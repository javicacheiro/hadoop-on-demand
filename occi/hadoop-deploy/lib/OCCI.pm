package OCCI;
use Moose;
use Carp;
use v5.10;


has 'endpoint' => ( is => 'rw', isa => 'Str', required => 1 );
has 'mixins' => ( is => 'rw', isa => 'Str', required => 1 );
has 'occi_cli' => (
	is => 'rw', 
	isa => 'Str',
	default => '/opt/occi-cli/bin/occi ',
);
has 'opts' => (
	is => 'rw', 
	isa => 'Str',
	default => " --auth x509 --user-cred /tmp/x509up_u509 --voms --timeout 180 ",
);
has 'context' => (
	is => 'rw', 
	isa => 'Str',
	default => q( public_key='ssh-dss AAAAB3NzaC1kc3MAAACBAOc0z4UJ/YhAireiQlfcbKb9RErhg67ebkMMQGCIphyBXL9bBJOOQdyt/CEmHArLgr2Z5k8k7UfhDfucn3qLzeMNp2txAk/iZ7u5ZmeLx+I8mRkLw0FNDg6ZyKVntGuV/NtRIk/px5kW40I7xD8Yr+ZuJ8V01GKMv2jLq+cWBqaPAAAAFQC4R7h1OQpNkSeRNRPyM3LUZL5BRwAAAIB5PfQR7DJ0lQcbIn3AVK82MniaDLlHPQBGCEs+oD9P7Ufr/8GPFGD6hMvQ2NCX2p6BL6fo1Eo7VEOTOZNRrGy1RFncOwWNnEU/FP3dIg1IHQ3lfjvn0eSLd4hwD/2qYMuvJlIlev1EU2PRU/6IfzHM1ZBQ7Od77vaizoP/l7YZKgAAAIEAu0V/5+05QQSmbKERbGCbr1Go9MMTP3ts61VF4MubsaXshITSM93/qUh1G9gkW34jXd379SLBcHTdQnyrr0i1iiGtSweBCQ+lGbV30nX6+g8RQBt144zy91GOyqF2fNhGEoACi4TkmDAG1wWE1ca6ybU0wbEBT3kgRrjMHtxhT90= jlopez@pcjesus.cesga.es' ),
);
has 'run_cmd' => ( 
	is => 'rw', 
	isa => 'Str',
	# TODO: Check why default sub is not able to access occi_cli, ... variables
	#default => sub {
	#	my $self = shift;
	#	return $self->occi_cli . " --endpoint " . $self->endpoint . $self->opts;
	#},
	#default => sub {
	#	my $self = shift;
	#	#return $self->occi_cli . " --endpoint " . $self->endpoint . " --context " . $self->context . $self->opts;
	#	say "Endpoint: " . $self->endpoint;
	#	say $self->occi_cli . " --endpoint " . $self->endpoint . $self->opts;
	#	return $self->occi_cli . " --endpoint " . $self->endpoint . $self->opts;
	#},
);
has 'instances' => (
	is => 'rw',
	isa => 'ArrayRef',
	default => sub { [] },
);
# Set the occi parameter to the base occi cmdline
sub BUILD {
	my $self = shift;
	$self->run_cmd($self->occi_cli . " --endpoint " . $self->endpoint . $self->opts);
	return;
	# TODO: Check why has_* methods do not exist
	$self->has_occi_cli and $self->has_endpoint and $self->has_opts and do {
		$self->run_cmd($self->occi_cli . " --endpoint " . $self->endpoint . $self->opts);
		return;
	};
	confess "At least occi_cli, endpoint, and opts must be available";
}


sub launch {
	@_ == 2 or croak "Bad number of arguments: run <N>";
	my ($self, $num) = @_;
	#say $self->occi;
	my @id;
	my $cmd = $self->run_cmd . " --context " . $self->context . " --action create --resource compute " . $self->mixins;
	for (my $i = 0; $i < $num; $i++) {
		my $cmd_with_name = $cmd . " --attribute occi.core.title='Hadoop-$i' ";
		#say $cmd;
		$id[$i] = `$cmd_with_name`;
		#$id[$i] = "2";
	}
	push @{ $self->instances }, @id;
	return @id;

}

sub list {
	@_ == 1 or croak "Bad number of arguments";
	my $self = shift;
	my $cmd = $self->run_cmd . " --action list --resource compute ";
	#say "list: run_cmd: " . $self->run_cmd;
	#say "list: Endpoint: " . $self->endpoint;
	#say "list: " . $cmd;
	return `$cmd`;
}

# Describe a given resource
sub describe {
	@_ == 2 or croak "Bad number of arguments";
	my ($self, $resource) = @_;
	my $cmd = $self->run_cmd . " --action describe --resource " . $resource;
	return `$cmd`;
}

# Get the IP a given instance
sub get_ip {
	@_ == 2 or croak "Bad number of arguments";
	my ($self, $resource) = @_;
	my $cmd = $self->run_cmd . " --action describe --resource " . $resource;
	my $description = $self->describe($resource);
	$description =~ /occi.networkinterface.address\s+=\s+(\d+\.\d+\.\d+\.\d+)\n.*occi.networkinterface.address\s+=\s+(\d+\.\d+\.\d+\.\d+)\n/s and do {
		return $2;
	};
	$description =~ /occi.networkinterface.address\s+=\s+(.*)/m and do {
		return $1;
	};
}

# Add a public IP to the instance
sub add_public_ip {
        @_ == 2 or croak "Bad number of arguments";
        my ($self, $resource) = @_;
        my $cmd = $self->run_cmd . " --action link --resource " . $resource . " --link /network/public";
	return `$cmd`;
}

# Delete a given resource
sub delete {
	@_ == 2 or croak "Bad number of arguments";
	my ($self, $resource) = @_;
	my $cmd = $self->run_cmd . " --action delete --resource " . $resource;
	return `$cmd`;
}

# Load list of resources from file
sub load {
	@_ == 2 or croak "Bad number of arguments";
	my ($self, $file) = @_;
	open(IN,'<',$file,);
	while(<IN>) {
		chomp;
		push @{ $self->instances }, $_;
	}
}

# Save list of resources to file
sub save {
	@_ == 2 or croak "Bad number of arguments";
	my ($self, $file) = @_;
	open(OUT,'>',$file,);
	for (@{ $self->instances }) {
		print OUT $_;
	}
}

no Moose;
__PACKAGE__->meta->make_immutable;

__END__

=pod

=encoding utf8

=head1 NAME

OCCI - Utilities to use fedcloud with OCCI

=head1 SYNOPSIS

  use OCCI;

  my $cesga = OCCI->new(
        endpoint => 'https://cloud.cesga.es:3202',
        mixins => '--mixin os_tpl#uuid_hadoop_1_2_1_raw_slave_fedcloud_255'
  );

  $cesga->launch(21);

  say $cesga->list;

  for (@{ $cesga->instances }) {
       $cesga->delete($_);
  }


=head1 DESCRIPTION

OCCI is a class to help using remote sites that suport OCCI REST API.
In case you are using FedCloud have a look at the Fedcloud module.

=head1 USAGE

This module provides methods for launching and deleting instances at a given OCCI endpoint.

=head1 CONSTRUCTORS

=over 4

=item new ( OPTIONS )

Construct a new OCCI object.
OPTIONS are passed in a hash like fashion, using key and value pairs.
Possible options are:

B<endpoint> - Endpoint URI of the OCCI service.

B<mixins> - Mixins (eg. image tempate) to use when launching new instances.

=back

=head1 METHODS

=over 4

=item launch ( NUMBER )

Launches the given NUMBER of new instances

=item delete( RESOURCE )

Deletes the given RESOURCE. It can be an VM instance or any other type of resource.

=item list ()

Returns the list of instances 

=back

=head1 AUTHORS

Javier Cacheiro <jlopez@cesga.es>

=head1 COPYRIGHT

Copyright (c) 2014 Javier Cacheiro. All rights reserved. This program is free software; you can
redistribute it and/or modify it under the same terms as Perl itself.

This program is free software; you can redistribute it and/or modify
it under the same terms as Perl itself.

=head1 SEE ALSO

=cut

