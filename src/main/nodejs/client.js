#!/usr/bin/env node
'use strict';

const eyes = require('eyes'), p = eyes.inspect.bind(eyes);

const thrift = require('thrift');
// const ThriftTransports = require('thrift/transport');
// const ThriftProtocols = require('thrift/protocol');
const Persistence = require('./gen-nodejs/Persistence');
const ttypes = require('./gen-nodejs/gelatin_types');
const net = require('net')

const transport = thrift.TBufferedTransport()
const protocol = thrift.TBinaryProtocol()

const stream = net.createConnection({
	path: "./server.sock"
})

const connection = new thrift.Connection(stream, {
	transport : transport,
	protocol : protocol
})

connection.on("connect", () => {
	p("SOMETHING")

	connection.on('error', function(err) {
	  p(`Connection error: ${err}`)
	})

	const client = thrift.createClient(Persistence, connection)

	client.getConfig((err, response) => {
	  if ( err ) throw err;
	  p(response)
	  connection.end()
	})
})



