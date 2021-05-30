let
  pkgs = import <nixpkgs> {};
  mill = pkgs.callPackage ./mill.nix {};
in
  pkgs.stdenv.mkDerivation {
    name = "scalaZioConcurDevEnv";
    buildInputs = [
      pkgs.curl
      pkgs.sbt
      pkgs.scalafmt # tend to use that supplied by sbt instead
      mill
    ];
    shellHook = ''
      unset SSL_CERT_FILE
    '';
  }
  
