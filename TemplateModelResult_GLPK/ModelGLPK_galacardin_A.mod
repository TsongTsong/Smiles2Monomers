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
 a0 a1 a2 a3 a4 a5 a6 a7 a8 a9 a10 a11 a12 a13 a14 a15 a16 a17 a18 a19 a20 a21 a22 a23 a24 a25 a26 a27 a28 a29 a30 a31 a32 a33 a34 a35 a36 a37 a38 a39 a40 a41 a42 a43 a44 a45 a46 a47 a48 a49 a50 a51 a52 a53 a54 a55 a56 a57 a58 a59 a60 a61 a62 a63 a64 a65 a66 a67 a68 a69 a70 a71 a72 a73 a74 a75 a76 a77 a78 a79 a80 a81 a82 a83 a84 a85 a86 a87 a88 a89 a90 a91 a92 a93 a94 a95 a96 a97 a98 a99 a100 a101 a102 a103 a104 a105 a106 a107 a108 a109 a110 a111 a112 a113 a114 a115 a116 a117 a118 a119 a120 a121 a122 a123 a124 a125 a126 a127 a128 a129 a130 a131 a132 a133 a134 a135 a136 a137 a138 a139 a140 a141 a142 a143 a144 a145 a146 a147 a148 a149 a150 a151 a152 a153 a154 a155 a156 a157
;
set Matches := 
 m0 m1 m2 m3 m4 m5 m6 m7 m8 m9 m10 m11 m12 m13 m14 m15 m16 m17 m18
;

param MatchSize :=
 m0 12
 m1 12
 m2 13
 m3 10
 m4 14
 m5 12
 m6 13
 m7 13
 m8 12
 m9 9
 m10 9
 m11 11
 m12 11
 m13 11
 m14 11
 m15 11
 m16 11
 m17 11
 m18 11
;
param AtomsOwners : 
   m0 m1 m2 m3 m4 m5 m6 m7 m8 m9 m10 m11 m12 m13 m14 m15 m16 m17 m18 :=
 a0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0
 a1 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0 0
 a2 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0 0
 a3 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0 0
 a4 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0 0
 a5 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0 0
 a6 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0 0
 a7 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0 0
 a8 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0 0
 a9 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0 0
 a10 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a11 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a12 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a13 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a14 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a15 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a16 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
 a17 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
 a18 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
 a19 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
 a20 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
 a21 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
 a22 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
 a23 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
 a24 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
 a25 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
 a26 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0
 a27 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a28 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a29 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a30 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a31 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0 0
 a32 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 1 0 0 0
 a33 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a34 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a35 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a36 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a37 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 1 0 0
 a38 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 1 0 0
 a39 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 1 0 0
 a40 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 1 0 0
 a41 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 1 0 0
 a42 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 1 0 0
 a43 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 1 0 0
 a44 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0
 a45 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0
 a46 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0
 a47 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0
 a48 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0
 a49 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0
 a50 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0
 a51 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0
 a52 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0
 a53 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0
 a54 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0
 a55 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 1 0 0
 a56 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 1 0 0
 a57 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0
 a58 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 1 0 0
 a59 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 1 0 0
 a60 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a61 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a62 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a63 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a64 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a65 1 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a66 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a67 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a68 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a69 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a70 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a71 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a72 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a73 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0
 a74 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a75 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a76 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a77 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a78 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a79 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a80 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a81 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a82 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a83 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a84 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0
 a85 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a86 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a87 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a88 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a89 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a90 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a91 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a92 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a93 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a94 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a95 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a96 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a97 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0
 a98 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0
 a99 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0
 a100 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0
 a101 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0
 a102 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0
 a103 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0
 a104 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0
 a105 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0
 a106 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0 0
 a107 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a108 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a109 1 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a110 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a111 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a112 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a113 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a114 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a115 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a116 0 0 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a117 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a118 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a119 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0
 a120 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0
 a121 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0
 a122 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0
 a123 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0
 a124 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0
 a125 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0
 a126 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0
 a127 0 0 0 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0 0
 a128 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a129 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a130 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1
 a131 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1
 a132 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1
 a133 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1
 a134 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a135 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 0
 a136 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1
 a137 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1
 a138 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1
 a139 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1
 a140 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1
 a141 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1
 a142 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1
 a143 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a144 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a145 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a146 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a147 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a148 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a149 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a150 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a151 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a152 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a153 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a154 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0 0
 a155 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a156 0 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
 a157 0 0 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
;
 
end;
