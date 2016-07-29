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
 a0 a1 a2 a3 a4 a5 a6 a7 a8 a9 a10 a11 a12 a13 a14 a15 a16 a17 a18 a19 a20 a21 a22 a23 a24 a25 a26 a27 a28 a29 a30 a31 a32 a33 a34 a35 a36 a37 a38 a39 a40 a41 a42 a43 a44 a45 a46 a47 a48 a49 a50 a51 a52 a53 a54 a55 a56 a57 a58 a59 a60 a61 a62 a63 a64 a65 a66 a67 a68 a69 a70 a71 a72 a73 a74 a75 a76 a77 a78 a79 a80 a81 a82 a83 a84 a85 a86 a87 a88 a89 a90 a91 a92 a93 a94 a95 a96 a97 a98 a99 a100 a101 a102 a103 a104 a105 a106 a107 a108 a109 a110 a111 a112 a113 a114 a115 a116 a117 a118 a119 a120 a121 a122 a123 a124 a125 a126 a127
;
set Matches := 
 m0 m1 m2 m3 m4 m5 m6 m7 m8 m9 m10 m11 m12 m13 m14 m15 m16 m17 m18 m19
;

param MatchSize :=
 m0 8
 m1 8
 m2 17
 m3 8
 m4 8
 m5 8
 m6 8
 m7 8
 m8 14
 m9 9
 m10 9
 m11 6
 m12 6
 m13 6
 m14 6
 m15 11
 m16 8
 m17 4
 m18 7
 m19 7
;
param AtomsOwners : 
   m0 m1 m2 m3 m4 m5 m6 m7 m8 m9 m10 m11 m12 m13 m14 m15 m16 m17 m18 m19 :=
 a0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a1 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a2 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a3 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a4 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a5 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a6 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0
 a7 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0
 a8 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0
 a9 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0
 a10 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0
 a11 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0
 a12 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0
 a13 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0
 a14 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0
 a15 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0
 a16 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0
 a17 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0
 a18 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0
 a19 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a20 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a21 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a22 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a23 0 0 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a24 0 0 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a25 0 0 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a26 0 0 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a27 0 0 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a28 0 0 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a29 0 0 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a30 0 0 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a31 0 0 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a32 0 0 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a33 0 0 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a34 0 0 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a35 0 0 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a36 0 0 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a37 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a38 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a39 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a40 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a41 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a42 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a43 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a44 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a45 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a46 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a47 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a48 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a49 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a50 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a51 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a52 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a53 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0
 a54 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0
 a55 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0
 a56 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0
 a57 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0
 a58 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0
 a59 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a60 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a61 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a62 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a63 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a64 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a65 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a66 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a67 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
 a68 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
 a69 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
 a70 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
 a71 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
 a72 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
 a73 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a74 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a75 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a76 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a77 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a78 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a79 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a80 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a81 0 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0
 a82 0 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0
 a83 0 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0
 a84 0 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0
 a85 0 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0
 a86 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0
 a87 0 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0
 a88 0 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0
 a89 0 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0
 a90 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0
 a91 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0
 a92 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0
 a93 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0
 a94 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0
 a95 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0
 a96 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a97 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a98 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a99 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a100 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a101 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a102 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a103 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a104 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a105 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a106 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a107 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a108 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a109 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a110 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 1
 a111 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 1
 a112 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 1
 a113 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 1
 a114 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 1
 a115 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 1
 a116 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 1
 a117 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 1 0 0 0
 a118 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a119 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a120 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a121 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a122 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a123 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a124 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a125 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a126 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a127 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
;
 
end;
