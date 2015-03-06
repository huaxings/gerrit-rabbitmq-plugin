// Copyright (C) 2013 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.googlesource.gerrit.plugins.rabbitmq;

import com.google.gerrit.common.EventListener;
import com.google.gerrit.extensions.events.LifecycleListener;
import com.google.gerrit.extensions.registration.DynamicSet;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.multibindings.Multibinder;

import com.googlesource.gerrit.plugins.rabbitmq.config.PluginProperties;
import com.googlesource.gerrit.plugins.rabbitmq.config.Properties;
import com.googlesource.gerrit.plugins.rabbitmq.config.PropertiesFactory;
import com.googlesource.gerrit.plugins.rabbitmq.config.section.AMQP;
import com.googlesource.gerrit.plugins.rabbitmq.config.section.Exchange;
import com.googlesource.gerrit.plugins.rabbitmq.config.section.Gerrit;
import com.googlesource.gerrit.plugins.rabbitmq.config.section.Message;
import com.googlesource.gerrit.plugins.rabbitmq.config.section.Monitor;
import com.googlesource.gerrit.plugins.rabbitmq.config.section.Section;
import com.googlesource.gerrit.plugins.rabbitmq.message.MessagePublisher;
import com.googlesource.gerrit.plugins.rabbitmq.message.Publisher;
import com.googlesource.gerrit.plugins.rabbitmq.message.PublisherFactory;
import com.googlesource.gerrit.plugins.rabbitmq.session.Session;
import com.googlesource.gerrit.plugins.rabbitmq.session.SessionFactory;
import com.googlesource.gerrit.plugins.rabbitmq.session.impl.AMQPSession;
import com.googlesource.gerrit.plugins.rabbitmq.solver.SolverImpl;
import com.googlesource.gerrit.plugins.rabbitmq.solver.Solver;
import com.googlesource.gerrit.plugins.rabbitmq.solver.SolverFactory;
import com.googlesource.gerrit.plugins.rabbitmq.worker.ChangeWorker;
import com.googlesource.gerrit.plugins.rabbitmq.worker.ChangeWorkerFactory;
import com.googlesource.gerrit.plugins.rabbitmq.worker.DefaultChangeWorker;
import com.googlesource.gerrit.plugins.rabbitmq.worker.UserChangeWorker;

class Module extends AbstractModule {

  @Override
  protected void configure() {
    Multibinder<Section> sectionBinder = Multibinder.newSetBinder(binder(), Section.class);
    sectionBinder.addBinding().to(AMQP.class);
    sectionBinder.addBinding().to(Exchange.class);
    sectionBinder.addBinding().to(Gerrit.class);
    sectionBinder.addBinding().to(Message.class);
    sectionBinder.addBinding().to(Monitor.class);

    install(new FactoryModuleBuilder().implement(Solver.class, SolverImpl.class).build(SolverFactory.class));
    install(new FactoryModuleBuilder().implement(Session.class, AMQPSession.class).build(SessionFactory.class));
    install(new FactoryModuleBuilder().implement(Publisher.class, MessagePublisher.class).build(PublisherFactory.class));
    install(new FactoryModuleBuilder().implement(Properties.class, PluginProperties.class).build(PropertiesFactory.class));
    install(new FactoryModuleBuilder().implement(ChangeWorker.class, UserChangeWorker.class).build(ChangeWorkerFactory.class));

    DynamicSet.bind(binder(), LifecycleListener.class).to(RabbitMQManager.class);
    DynamicSet.bind(binder(), EventListener.class).to(DefaultChangeWorker.class);
  }
}
