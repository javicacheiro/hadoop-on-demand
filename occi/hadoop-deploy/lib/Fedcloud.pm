package Fedcloud;
use OCCI;
use Carp;

use Exporter qw(import);

our $VERSION = '0.10';

# Symbols that can be imported
our @EXPORT_OK = qw($cesga $bifi $ifca $cesnet);
# Symbols exported by default
our @EXPORT = qw($cesga $bifi $ifca $cesnet);

our $cesga = OCCI->new( 
       endpoint => 'https://cloud.cesga.es:3202', 
       mixins => '--mixin os_tpl#uuid_hadoop_1_2_1_raw_slave_fedcloud_255',
);

our $bifi = OCCI->new(
        endpoint => 'http://server4-epsh.unizar.es:8787',
        mixins => '--mixin os_tpl#37c0680c-44f8-44c3-9a6d-022629a5f125 --mixin resource_tpl#hadoop_fedcloud_ephemeral',
);

our $ifca = OCCI->new(
        endpoint => 'https://cloud.ifca.es:8787',
        mixins => '--mixin os_tpl#091492b5-59fe-436c-9744-d31c1e0f4f4c --mixin resource_tpl#cf1-small',
);

our $cesnet = OCCI->new(
        endpoint => 'https://carach5.ics.muni.cz:11443',
        mixins => '--mixin os_tpl#uuid_hadoop_sl6_raw_1_2_1_fedcloud_dukan_77',
);




1;

__END__

=pod

=encoding utf8

=head1 NAME

Fedcloud - Utilities to use fedcloud with OCCI

=head1 SYNOPSIS

  use Fedcloud;

  $cesga->launch(2);

=head1 DESCRIPTION

Fedcloud is a container with the connection detail of all sites in Fedcloud, so you can use them easily.

=head1 USAGE

This module provides the following class methods:

=head2 Fedcloud->list()

Returns the list of supported sites.

=head1 AUTHORS

Javier Cacheiro <jlopez@cesga.es>

=head1 COPYRIGHT

Copyright (c) 2014 Javier Cacheiro. All rights reserved. This program is free software; you can
redistribute it and/or modify it under the same terms as Perl itself.

This program is free software; you can redistribute it and/or modify
it under the same terms as Perl itself.

=head1 SEE ALSO

=cut
