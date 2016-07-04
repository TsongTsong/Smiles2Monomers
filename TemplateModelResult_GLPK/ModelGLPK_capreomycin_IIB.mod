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
 a0 a1 a2 a3 a4 a5 a6 a7 a8 a9 a10 a11 a12 a13 a14 a15 a16 a17 a18 a19 a20 a21 a22 a23 a24 a25 a26 a27 a28 a29 a30 a31 a32 a33 a34 a35 a36
;
set Matches := 
 m0 m1 m2 m3 m4
;

param MatchSize :=
 m0 6
 m1 6
 m2 5
 m3 11
 m4 9
;
param AtomsOwners : 
   m0 m1 m2 m3 m4 :=
 a0 0 0 1 0 0
 a1 0 0 1 0 0
 a2 0 0 1 0 0
 a3 1 0 0 0 0
 a4 1 0 0 0 0
 a5 1 0 0 0 0
 a6 1 0 0 0 0
 a7 1 0 0 0 0
 a8 1 0 0 0 0
 a9 0 0 0 1 0
 a10 0 0 0 1 0
 a11 0 0 0 1 0
 a12 0 0 0 1 0
 a13 0 0 0 0 1
 a14 0 0 0 0 1
 a15 0 0 0 0 1
 a16 0 0 0 0 1
 a17 0 0 0 0 1
 a18 0 0 0 0 1
 a19 0 0 0 0 1
 a20 0 0 0 0 1
 a21 0 0 0 0 1
 a22 0 1 0 0 0
 a23 0 1 0 0 0
 a24 0 1 0 0 0
 a25 0 1 0 0 0
 a26 0 1 0 0 0
 a27 0 1 0 0 0
 a28 0 0 1 0 0
 a29 0 0 1 0 0
 a30 0 0 0 1 0
 a31 0 0 0 1 0
 a32 0 0 0 1 0
 a33 0 0 0 1 0
 a34 0 0 0 1 0
 a35 0 0 0 1 0
 a36 0 0 0 1 0
;
 
end;
