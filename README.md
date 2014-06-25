gerrit-rabbitmq-plugin: Gerrit event publish plugin via RabbitMQ
=======================

* Author: rinrinne a.k.a. rin_ne
* Repository: http://github.com/rinrinne/gerrit-rabbitmq-plugin

[![Build Status](https://travis-ci.org/rinrinne/gerrit-rabbitmq-plugin.png?branch=dev-1.0)](https://travis-ci.org/rinrinne/gerrit-rabbitmq-plugin)

Synopsis
----------------------

This is Gerrit plugin.

This can publish gerrit events to message queue provided by RabbitMQ.
Published events are the same as Gerrit stream evnets.

This plugin works on Gerrit 2.8 or later.

*NOTE*: Here is `dev-1.0` branch. This is available on master in gerrit. Supported Buck build only.

About Buck
---------------------

[Buck] is a build system now gerrit adopt. If you want to use Buck,
you need to setup it referring [Building with Buck] in gerrit documentation.

[Buck]: http://facebook.github.io/buck/
[Building with Buck]: https://gerrit-documentation.storage.googleapis.com/Documentation/2.8.5/dev-buck.html


Environments
---------------------

* `linux`
  * `java-1.7`
    * `buck`

Build
---------------------

* Use `buck`

To build plugin with buck

    git clone https://gerrit.googlesource.com/gerrit
    ln -s $(pwd) gerrit/plugins/rabbitmq
    cd gerrit
    buck build plugins/rabbitmq:rabbitmq

Reference
---------------------

* [Configuration]
* [Message Format]

[Configuration]: https://github.com/rinrinne/gerrit-rabbitmq-plugin/blob/master/src/main/resources/Documentation/config.md
[Message Format]: https://github.com/rinrinne/gerrit-rabbitmq-plugin/blob/master/src/main/resources/Documentation/message.md

Minimum Configuration
---------------------

```
  [amqp]
    uri = amqp://localhost
  [exchange]
    name = exchange-for-gerrit-queue
  [message]
    routingKey = com.foobar.www.gerrit
  [gerrit]
    name = foobar-gerrit
    hostname = www.foobar.com
```

License
---------------------

The Apache Software License, Version 2.0

Copyright
---------------------

Copyright (c) 2013 rinrinne a.k.a. rin_ne
