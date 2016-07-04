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
 a0 a1 a2 a3 a4 a5 a6 a7 a8 a9 a10 a11 a12 a13 a14 a15 a16 a17 a18 a19 a20 a21 a22 a23 a24 a25 a26 a27 a28 a29 a30 a31 a32 a33 a34 a35 a36 a37 a38 a39 a40 a41 a42 a43 a44 a45 a46 a47 a48 a49 a50 a51 a52 a53 a54 a55 a56 a57 a58 a59 a60 a61 a62 a63 a64 a65 a66 a67 a68 a69 a70 a71 a72 a73 a74 a75 a76 a77 a78 a79 a80 a81 a82 a83 a84 a85 a86 a87 a88 a89 a90 a91 a92 a93 a94 a95 a96 a97 a98 a99 a100 a101 a102 a103
;
set Matches := 
 m0 m1 m2 m3 m4 m5 m6 m7 m8 m9 m10 m11 m12 m13 m14
;

param MatchSize :=
 m0 18
 m1 12
 m2 12
 m3 8
 m4 8
 m5 8
 m6 8
 m7 9
 m8 9
 m9 8
 m10 5
 m11 9
 m12 8
 m13 7
 m14 7
;
param AtomsOwners : 
   m0 m1 m2 m3 m4 m5 m6 m7 m8 m9 m10 m11 m12 m13 m14 :=
 a0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a2 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a3 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a4 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a5 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a6 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a7 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a8 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a9 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a10 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a11 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a12 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a13 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a14 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a15 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a16 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a17 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a18 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0
 a19 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0
 a20 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0
 a21 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0
 a22 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0
 a23 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0
 a24 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
 a25 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0
 a26 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0
 a27 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a28 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a29 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a30 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a31 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a32 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a33 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a34 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a35 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0
 a36 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0
 a37 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0
 a38 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0
 a39 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0
 a40 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0
 a41 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0
 a42 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0
 a43 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0
 a44 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0
 a45 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a46 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a47 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a48 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a49 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a50 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a51 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a52 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a53 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a54 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a55 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a56 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a57 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a58 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a59 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a60 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a61 0 0 0 0 0 1 0 0 0 0 0 1 0 0 0
 a62 0 0 0 0 0 1 0 0 0 0 0 1 0 0 0
 a63 0 0 0 0 0 1 0 0 0 0 0 1 0 0 0
 a64 0 0 0 0 0 1 0 0 0 0 0 1 0 0 0
 a65 0 0 0 0 0 1 0 0 0 0 0 1 0 0 0
 a66 0 0 0 0 0 1 0 0 0 0 0 1 0 0 0
 a67 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0
 a68 0 0 0 0 0 1 0 0 0 0 0 1 0 0 0
 a69 0 0 0 0 0 1 0 0 0 0 0 1 1 0 0
 a70 0 0 0 0 0 0 0 0 0 0 0 0 1 0 1
 a71 0 0 0 0 0 0 0 0 0 0 0 0 1 0 1
 a72 0 0 0 0 0 0 0 0 0 0 0 0 1 0 1
 a73 0 0 0 0 0 0 0 0 0 0 0 0 1 0 1
 a74 0 0 0 0 0 0 0 0 0 0 0 0 1 0 1
 a75 0 0 0 0 0 0 0 0 0 0 0 0 1 0 1
 a76 0 0 0 0 0 0 0 0 0 0 0 0 1 0 1
 a77 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a78 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a79 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a80 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a81 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a82 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0
 a83 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0
 a84 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0
 a85 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0
 a86 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0
 a87 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0
 a88 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0
 a89 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0
 a90 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0
 a91 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a92 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a93 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a94 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a95 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0
 a96 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0
 a97 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a98 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a99 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a100 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a101 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a102 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a103 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
;
 
end;
