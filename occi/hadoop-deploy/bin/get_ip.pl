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


my %sites = ( 
	#cesga => $cesga, 
	#ifca => $ifca, 
	bifi => $bifi, 
	#cesnet => $cesnet,
);

for my $name (keys %sites) {
	my $site = $sites{$name};
	$site->load("cache/$name/resources");
	say "= ". uc $name ." =";
	for (@{ $site->instances }) {
		say $site->get_ip($_);
	}
}

