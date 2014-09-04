#!/usr/bin/env perl
use strict;
use warnings;
use v5.10;

use OCCI;

#
# BIFI
my $bifi = OCCI->new( 
	endpoint => 'http://server4-epsh.unizar.es:8787', 
	mixins => '--mixin os_tpl#37c0680c-44f8-44c3-9a6d-022629a5f125 --mixin resource_tpl#hadoop_fedcloud_ephemeral' 
);

say "= BIFI =";
say $bifi->list();
my @vm_list = $bifi->launch(20);
say "@vm_list";
#say "= List =";
#say $bifi->list();
$bifi->save('cache/bifi/resources');

#$bifi->load('cache/bifi/resources');

#say "@{ $bifi->instances }";

#say $bifi->describe('https://carach5.ics.muni.cz:11443/compute/41912');

#for (@{ $bifi->instances }) {
#	#$bifi->describe($_);
#	$bifi->delete($_);
#}

