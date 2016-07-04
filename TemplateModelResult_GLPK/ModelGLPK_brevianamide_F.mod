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
 a0 a1 a2 a3 a4 a5 a6 a7 a8 a9 a10 a11 a12 a13 a14 a15 a16 a17 a18 a19 a20
;
set Matches := 
 m0 m1 m2
;

param MatchSize :=
 m0 14
 m1 8
 m2 7
;
param AtomsOwners : 
   m0 m1 m2 :=
 a0 0 1 1
 a1 0 1 1
 a2 1 1 0
 a3 1 0 0
 a4 1 0 0
 a5 1 0 0
 a6 1 0 0
 a7 1 0 0
 a8 1 0 0
 a9 1 0 0
 a10 1 0 0
 a11 1 0 0
 a12 1 0 0
 a13 1 0 0
 a14 1 0 0
 a15 1 0 0
 a16 0 1 1
 a17 0 1 1
 a18 0 1 1
 a19 0 1 1
 a20 0 1 1
;
 
end;
