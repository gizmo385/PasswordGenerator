PasswordGenerator
=================

A password generator in Java


SeedLocker Branch info:
=======================

This branch will implement a way for users to securely store their seeds and patterns alongside their client. The seeds and patterns will all be encrypted and will require a password to decrypt. This password will not be stored in it's raw form on the client's machine but will instead be saved as a hash. The password, when correctly entered will be used to decrypt the seeds and patterns which will be encrypted using the password as a key.
