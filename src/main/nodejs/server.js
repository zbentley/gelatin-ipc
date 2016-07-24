#!/usr/bin/env node
'use strict';

const eyes = require('eyes'), p = eyes.inspect.bind(eyes);

const thrift = require("thrift"),
	Persistence = require("./gen-nodejs/Persistence"),
	ttypes = require("./gen-nodejs/gelatin_types"),
	fs = require('fs-extra'),
	path = require('path')
const net = require('net')

const SOCKET = path.resolve('./server.sock');

//fs.removeSync(SOCKET)

const server = thrift.createServer(Persistence, {
	getConfig: (cb) => {
		p(null, "foooo");
		cb(null, {});
	},
	// start: () => {

	// },
});

function reportServerError(e) {
	throw e;
}

function startServer() {

}

server.listen(SOCKET)

server.on('error', (err) => {
    if (err.code === 'EADDRINUSE') {
    	const stream = net.createConnection({
			path: SOCKET
		})
		stream.on('error', (e) => {
			if(e.code === 'ECONNREFUSED') {
				fs.removeSync(SOCKET)
				server.listen(SOCKET, startServer)
			}
		})
    } else {
    	reportServerError(err)
    }
})

server.on('connection', (s) => {
	p(`Got a connection from ${s}`)
})