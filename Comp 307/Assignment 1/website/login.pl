#!/usr/bin/perl
# Read the standard input (sent by the form):
read(STDIN, $input, $ENV{CONTENT_LENGTH});
# Get the name and value for each form input:
@pairs = split(/&/, $input);
# Then for each name/value pair....
foreach $pair (@pairs) {
     ($name, $value) = split(/=/, $pair);
     $value =~ tr/+/ /;
     $value =~ s/%([\dA-Fa-f]{2})/pack("C", hex($1))/eg;
     $INPUT{$name} = $value;
}


print "Content-type: text/plain\n\n";

print "Your Username  is '$INPUT{username}' and your Password is '$INPUT{password}'";
