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
 a0 a1 a2 a3 a4 a5 a6 a7 a8 a9 a10 a11 a12 a13 a14 a15 a16 a17 a18 a19 a20 a21 a22 a23 a24 a25 a26 a27 a28 a29 a30 a31 a32 a33 a34 a35 a36 a37 a38 a39 a40 a41 a42 a43 a44 a45 a46
;
set Matches := 
 m0 m1 m2 m3 m4 m5 m6 m7 m8 m9 m10 m11 m12 m13 m14 m15 m16 m17 m18 m19 m20
;

param MatchSize :=
 m0 11
 m1 8
 m2 6
 m3 8
 m4 7
 m5 4
 m6 6
 m7 5
 m8 5
 m9 5
 m10 7
 m11 7
 m12 8
 m13 8
 m14 5
 m15 13
 m16 13
 m17 14
 m18 2
 m19 2
 m20 2
;
param AtomsOwners : 
   m0 m1 m2 m3 m4 m5 m6 m7 m8 m9 m10 m11 m12 m13 m14 m15 m16 m17 m18 m19 m20 :=
 a0 0 0 1 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0
 a1 0 0 1 0 0 0 0 1 0 0 0 0 0 0 1 0 0 0 0 0 0
 a2 0 0 1 0 0 1 0 1 0 0 0 0 0 0 1 0 0 0 0 0 0
 a3 0 0 1 0 0 1 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0
 a4 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a5 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a6 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a7 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a8 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a9 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a10 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a11 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a12 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a13 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a14 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a15 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a16 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a17 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a18 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a19 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a20 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a21 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a22 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a23 0 0 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a24 0 0 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a25 0 0 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a26 0 0 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a27 0 0 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a28 0 0 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a29 0 0 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a30 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a31 0 0 1 0 0 1 0 1 0 0 0 0 0 0 1 0 0 0 1 0 0
 a32 0 0 1 0 0 1 0 1 0 0 0 0 0 0 1 0 0 0 1 0 0
 a33 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 1 1 1 0 0 0
 a34 0 0 0 0 0 0 1 0 1 1 1 1 1 1 0 1 1 1 0 0 0
 a35 0 0 0 0 0 0 1 0 0 0 1 1 1 1 0 1 1 1 0 0 0
 a36 0 0 0 0 0 0 1 0 0 0 1 1 1 1 0 1 1 1 0 0 0
 a37 0 0 0 0 0 0 1 0 0 0 0 0 1 1 0 1 1 1 0 0 0
 a38 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 1 1 1 0 0 0
 a39 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 1 1 0 0 0
 a40 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 1 1 0 0 0
 a41 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 1 1 0 0 0
 a42 0 0 0 0 0 0 0 0 1 1 1 1 1 1 0 1 1 1 0 0 0
 a43 0 0 0 0 0 0 0 0 1 1 1 1 1 1 0 1 1 1 0 0 0
 a44 0 0 0 0 0 0 0 0 1 1 1 1 1 1 0 1 1 1 0 1 1
 a45 0 0 0 0 0 0 0 0 1 0 1 0 1 0 0 1 0 1 0 1 0
 a46 0 0 0 0 0 0 0 0 0 1 0 1 0 1 0 0 1 1 0 0 1
;
 
end;
