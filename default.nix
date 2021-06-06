let
  pkgs = import <nixpkgs> {};
  mill = pkgs.callPackage ./mill.nix {};
in
  pkgs.stdenv.mkDerivation {
    name = "scalaZioConcurDevEnv";
    buildInputs = [
      pkgs.curl
      pkgs.jdk11 # metals needs jdk between 8 and 11 inclusive
      pkgs.sbt
      pkgs.scalafmt # tend to use that supplied by sbt instead
      mill
    ];
    shellHook = ''
      unset SSL_CERT_FILE
      export JAVA_HOME=${pkgs.jdk11.out}
    '';
  }
  
