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
 a0 a1 a2 a3 a4 a5 a6 a7 a8 a9 a10 a11 a12 a13 a14 a15 a16 a17 a18 a19 a20 a21 a22 a23 a24 a25 a26 a27 a28 a29 a30
;
set Matches := 
 m0 m1 m2 m3 m4
;

param MatchSize :=
 m0 14
 m1 5
 m2 5
 m3 8
 m4 7
;
param AtomsOwners : 
   m0 m1 m2 m3 m4 :=
 a0 0 1 0 0 0
 a1 0 1 0 0 0
 a2 0 1 0 0 0
 a3 0 0 1 0 0
 a4 0 0 1 0 0
 a5 0 0 1 0 0
 a6 0 0 1 0 0
 a7 0 0 1 1 0
 a8 0 0 0 1 1
 a9 0 0 0 1 1
 a10 0 0 0 1 1
 a11 0 0 0 1 1
 a12 0 0 0 1 1
 a13 0 0 0 1 1
 a14 0 0 0 1 1
 a15 1 0 0 0 0
 a16 1 0 0 0 0
 a17 1 0 0 0 0
 a18 1 0 0 0 0
 a19 1 0 0 0 0
 a20 1 0 0 0 0
 a21 1 0 0 0 0
 a22 1 0 0 0 0
 a23 1 0 0 0 0
 a24 1 0 0 0 0
 a25 1 0 0 0 0
 a26 1 0 0 0 0
 a27 1 0 0 0 0
 a28 1 0 0 0 0
 a29 0 1 0 0 0
 a30 0 1 0 0 0
;
 
end;
