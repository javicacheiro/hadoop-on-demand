#!/usr/bin/env perl
use strict;
use warnings;
use v5.10;

# Include our lib dir
use File::Basename qw(dirname);
use Cwd qw(abs_path);
use lib dirname(dirname abs_path $0) . '/lib';

# Load fedcloud sites
use Fedcloud qw( $cesga $ifca $bifi $cesnet );



$ifca->load('cache/ifca/resources');
for (@{ $bifi->instances }) {
       $bifi->delete($_);
}

#
# Run
say "= IFCA ="
say "== Initial instances =="
say $ifca->list();
say "== Launching =="
my @instances_ifca = $ifca->launch(20);
say "== Launched instances =="
say "@instances_ifca";
$ifca->save('cache/ifca/resources');


# Get IPs


#my $bifi = OCCI->new( 
#	endpoint => 'http://server4-epsh.unizar.es:8787', 
#	mixins => '--mixin os_tpl#37c0680c-44f8-44c3-9a6d-022629a5f125 --mixin resource_tpl#hadoop_fedcloud_ephemeral' 
#);

#my @id = $bifi->run(2);
#
#say @id;

#$bifi->list();

#
# CESGA
#my $cesga = OCCI->new( 
#	endpoint => 'https://cloud.cesga.es:3202', 
#	mixins => '--mixin os_tpl#uuid_hadoop_1_2_1_raw_slave_fedcloud_255',
#);


#say "= CESGA =";
#say $cesga->list();
#my @vm_list = $cesga->launch(1);
#say "@vm_list";
#$cesga->load('cache/cesnet/resources');
#say "@{ $cesga->instances }";
#$cesga->save('cache/cesga/resources');

#exit;

#
# CESNET
my $cesnet = OCCI->new( 
	endpoint => 'https://carach5.ics.muni.cz:11443', 
	mixins => '--mixin os_tpl#uuid_hadoop_sl6_raw_1_2_1_fedcloud_dukan_77' 
);

say "= CESNET =";
say $cesnet->list();
my @vm_list = $cesnet->launch(20);
say "@vm_list";
#say "= List =";
#say $cesnet->list();
$cesnet->save('cache/cesnet/resources');

#$cesnet->load('cache/cesnet/resources');

#say "@{ $cesnet->instances }";

#say $cesnet->describe('https://carach5.ics.muni.cz:11443/compute/41912');

#for (@{ $cesnet->instances }) {
#	#$cesnet->describe($_);
#	$cesnet->delete($_);
#}

