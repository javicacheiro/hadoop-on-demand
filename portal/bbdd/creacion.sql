CREATE DATABASE IF NOT EXISTS hadoop;
use hadoop;

-- ----------- --
-- DROP TABLES --
-- ----------- --
DROP TABLE IF EXISTS keys_users;
DROP TABLE IF EXISTS node;
DROP TABLE IF EXISTS cluster;
DROP TABLE IF EXISTS sshkey;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS ips_users;
DROP TABLE IF EXISTS ip;

-- ------------- --
-- CREATE TABLES --
-- ------------- --
CREATE TABLE user(
	_id SERIAL,
	username VARCHAR(45),
	authLevel TINYINT DEFAULT 0, -- -1 banned, 0 no admin, 1 admin, 2 superadmin
	clusterLimit INTEGER DEFAULT -1, -- -1 no limit, 0 no clusters, n clusters
	vmLimit INTEGER DEFAULT -1 -- -1 no limit, 0 no vms, n vms
);


-- Se hace en 2 1-n porque el tamaño maximo permitido de la clave primaria en mysql
-- da problemas de lo contrario.
CREATE TABLE sshkey(
	_id SERIAL,
	sshkey TEXT
);

CREATE TABLE keys_users(
	idUser BIGINT(20) unsigned NOT NULL,
	idKey BIGINT(20) unsigned NOT NULL,
	FOREIGN KEY (idUser)
		REFERENCES user(_id),
	FOREIGN KEY (idKey)
		REFERENCES sshkey(_id),
	PRIMARY KEY (idUser, idKey)
);


CREATE TABLE cluster(
	_id BIGINT(20) unsigned PRIMARY KEY, -- cluster id
	userId BIGINT(20) unsigned NOT NULL,
	name VARCHAR(45),
	passwd VARCHAR(45),
	version VARCHAR(45), -- hadoop version
	size INTEGER unsigned,
	replication INTEGER unsigned,
	blocksize INTEGER unsigned, -- MB
	reduceTasks INTEGER,
	submitTime DATETIME,
	stopTime DATETIME,
	exitStatus INTEGER,
	FOREIGN KEY (userId)
		REFERENCES user(_id)
);

CREATE TABLE node(
	_id BIGINT(20) unsigned NOT NULL,-- node vmid
	clusterId BIGINT(20) unsigned NOT NULL,
	memory VARCHAR(45),
	cpu INTEGER,
	diskSize DOUBLE PRECISION,
	startTime DATETIME,
	endTime DATETIME,
	FOREIGN KEY (clusterId)
		REFERENCES cluster(_id),
	CONSTRAINT pk_node
		PRIMARY KEY (_id,clusterId)
);

CREATE TABLE ip(
	ip VARCHAR(15),
	idUser BIGINT(20) unsigned NOT NULL,
	FOREIGN KEY (idUser)
		REFERENCES user(_id),
	CONSTRAINT pk_ip
		PRIMARY KEY(ip,idUser)
);

-- ------------------- --
-- INSERT DEFAULT DATA --
-- ------------------- --

-- superadmin --
INSERT INTO user (username, authLevel, clusterLimit, vmLimit) VALUES ('superadmin',2,0,0);
INSERT INTO sshkey (sshkey) VALUES ('5aedbf70787ca09df3335d5093ca9e3c');
INSERT INTO keys_users (idUser, idKey) VALUES (1,1);