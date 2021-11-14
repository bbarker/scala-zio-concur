let
  pkgs = import <nixpkgs> {};
in
  pkgs.stdenv.mkDerivation {
    name = "scalaZioConcurDevEnv";
    buildInputs = [
      pkgs.curl
      pkgs.jdk11 # metals needs jdk between 8 and 11 inclusive
      pkgs.nodejs
      pkgs.sbt
      pkgs.scalafmt # tend to use that supplied by sbt instead
    ];
    shellHook = ''
      unset SSL_CERT_FILE
      export JAVA_HOME=${pkgs.jdk11.out}
    '';
  }

