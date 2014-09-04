#!/usr/bin/env perl
use strict;
use warnings;
use v5.10;

use OCCI;

#
# CESGA
my $cesga = OCCI->new( 
	endpoint => 'https://cloud.cesga.es:3202', 
	mixins => '--mixin os_tpl#uuid_hadoop_1_2_1_raw_slave_fedcloud_255' 
);

say "= CESGA =";
say $cesga->list();
my @vm_list = $cesga->launch(21);
say "@vm_list";
#say "= List =";
#say $cesga->list();
$cesga->save('cache/cesga/resources');

$cesga->load('cache/cesga/resources');

say "@{ $cesga->instances }";

#say $cesga->describe('https://carach5.ics.muni.cz:11443/compute/41912');

#for (@{ $cesga->instances }) {
#	#$cesga->describe($_);
#	$cesga->delete($_);
#}

