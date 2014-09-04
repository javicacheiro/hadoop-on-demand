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


say "= IFCA =";
say $ifca->list();
my @vm_list = $ifca->launch(20);
say "@vm_list";
#say "= List =";
#say $ifca->list();
$ifca->save('cache/ifca/resources');

#$ifca->load('cache/ifca/resources');

#say "@{ $ifca->instances }";

#say $ifca->describe('https://carach5.ics.muni.cz:11443/compute/41912');

#for (@{ $ifca->instances }) {
#	#$ifca->describe($_);
#	$ifca->delete($_);
#}

