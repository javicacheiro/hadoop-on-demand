#!/usr/bin/env perl
use strict;
use warnings;
open(IN,'tmp.txt');
while(<IN>) {
    /warning: CRLF will be replaced by LF in (.*)\.$/ and do {
        my $filename = $1;
        $filename =~ s/portal\/web\/web_public\///;
        print "Changing $filename\n";
        `dos2unix $filename`;
    };
}
