set Atoms;
set Matches;

param MatchSize{m in Matches};
param AtomsOwners{a in Atoms, m in Matches}, default 0;

var install{m in Matches}, binary;

s.t. Atom_Owner{a in Atoms}:
	sum{m in Matches} install[m] * AtomsOwners[a, m] <= 1;
	
maximize number_of_installed_atoms:
	sum{m in Matches} install[m] * MatchSize[m];
	
data;

set Atoms := 
 a0 a1 a2 a3 a4 a5 a6 a7 a8 a9 a10 a11 a12 a13 a14 a15 a16 a17 a18 a19 a20 a21 a22 a23 a24 a25 a26 a27 a28 a29 a30 a31 a32 a33 a34 a35 a36 a37 a38 a39 a40 a41 a42 a43 a44 a45 a46 a47 a48 a49 a50 a51 a52 a53 a54 a55 a56 a57 a58
;
set Matches := 
 m0 m1 m2 m3 m4 m5 m6 m7 m8 m9
;

param MatchSize :=
 m0 11
 m1 8
 m2 9
 m3 7
 m4 8
 m5 9
 m6 22
 m7 23
 m8 9
 m9 10
;
param AtomsOwners : 
   m0 m1 m2 m3 m4 m5 m6 m7 m8 m9 :=
 a0 0 0 0 1 0 0 0 0 0 0
 a1 0 0 0 1 0 0 0 0 0 0
 a2 0 0 0 1 0 0 0 0 0 0
 a3 0 0 0 1 0 0 0 0 0 0
 a4 0 0 0 1 0 0 0 0 0 0
 a5 0 0 0 0 1 1 0 0 1 1
 a6 0 0 0 0 1 1 0 0 1 1
 a7 0 0 0 0 1 1 0 0 1 1
 a8 0 0 0 0 1 1 0 0 1 1
 a9 0 0 0 0 0 1 0 0 0 1
 a10 0 0 0 0 1 1 0 0 1 1
 a11 0 0 0 0 1 1 0 0 1 1
 a12 0 0 0 0 1 1 0 0 1 1
 a13 0 0 0 0 1 1 0 0 1 1
 a14 1 0 0 0 0 0 0 0 1 1
 a15 1 0 0 0 0 0 0 0 0 0
 a16 1 0 0 0 0 0 0 0 0 0
 a17 1 0 0 0 0 0 0 0 0 0
 a18 1 0 0 0 0 0 0 0 0 0
 a19 1 0 0 0 0 0 0 0 0 0
 a20 1 0 0 0 0 0 0 0 0 0
 a21 1 0 0 0 0 0 0 0 0 0
 a22 1 0 0 0 0 0 0 0 0 0
 a23 1 0 0 0 0 0 0 0 0 0
 a24 1 0 0 0 0 0 0 0 0 0
 a25 0 0 0 0 0 0 1 1 0 0
 a26 0 0 0 0 0 0 1 1 0 0
 a27 0 0 0 0 0 0 1 1 0 0
 a28 0 0 0 0 0 0 1 1 0 0
 a29 0 0 0 0 0 0 1 1 0 0
 a30 0 0 0 0 0 0 1 1 0 0
 a31 0 0 0 0 0 0 1 1 0 0
 a32 0 0 0 0 0 0 1 1 0 0
 a33 0 0 0 0 0 0 1 1 0 0
 a34 0 0 0 0 0 0 1 1 0 0
 a35 0 0 0 0 0 0 1 1 0 0
 a36 0 0 0 0 0 0 1 1 0 0
 a37 0 0 0 0 0 0 1 1 0 0
 a38 0 0 0 0 0 0 1 1 0 0
 a39 0 0 0 0 0 0 1 1 0 0
 a40 0 0 0 0 0 0 1 1 0 0
 a41 0 0 0 0 0 0 1 1 0 0
 a42 0 0 0 0 0 0 1 1 0 0
 a43 0 0 0 0 0 0 0 1 0 0
 a44 0 0 0 0 0 0 1 1 0 0
 a45 0 0 0 0 0 0 1 1 0 0
 a46 0 0 0 0 0 0 1 1 0 0
 a47 0 0 0 0 0 0 1 1 0 0
 a48 0 1 1 0 0 0 0 0 0 0
 a49 0 1 1 0 0 0 0 0 0 0
 a50 0 1 1 0 0 0 0 0 0 0
 a51 0 1 1 0 0 0 0 0 0 0
 a52 0 1 1 0 0 0 0 0 0 0
 a53 0 1 1 0 0 0 0 0 0 0
 a54 0 0 0 1 0 0 0 0 0 0
 a55 0 0 0 1 0 0 0 0 0 0
 a56 0 1 1 0 0 0 0 0 0 0
 a57 0 1 1 0 0 0 0 0 0 0
 a58 0 0 1 0 0 0 0 0 0 0
;
 
end;
