begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|SignedPushPreReceiveHook
operator|.
name|keyIdToString
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|bcpg
operator|.
name|ArmoredInputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPPublicKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPPublicKeyRing
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|operator
operator|.
name|bc
operator|.
name|BcKeyFingerprintCalculator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_class
DECL|class|SignedPushPreReceiveHookTest
specifier|public
class|class
name|SignedPushPreReceiveHookTest
block|{
comment|// ./pubring.gpg
comment|// -------------
comment|// pub   1024R/30A5A053 2015-06-16 [expires: 2015-06-17]
comment|//       Key fingerprint = 96D6 DE78 E6D8 DA49 9387  1F31 FA09 A0C4 30A5 A053
comment|// uid                  A U. Thor<a_u_thor@example.com>
comment|// sub   1024R/D6831DC8 2015-06-16 [expires: 2015-06-17]
DECL|field|PUBKEY
specifier|private
specifier|static
specifier|final
name|String
name|PUBKEY
init|=
literal|"-----BEGIN PGP PUBLIC KEY BLOCK-----\n"
operator|+
literal|"Version: GnuPG v1\n"
operator|+
literal|"\n"
operator|+
literal|"mI0EVYCBUQEEALCKzuY6M68RRRm6PS1F322lpHSHTdW9PIURm5B//tbfS32EN6lM\n"
operator|+
literal|"ISwJxhanpZanv2o4mbV3V8oLT3jMVDPJ3dqmOZJdJs37l+dxCVJ3ycFe1LHtT2oT\n"
operator|+
literal|"eRyC5PxD7UY5PdDe97mjp7yrp/bx1hE6XqGV0nDGrkJXc8A35u3WzIF5ABEBAAG0\n"
operator|+
literal|"IEEgVS4gVGhvciA8YV91X3Rob3JAZXhhbXBsZS5jb20+iL4EEwECACgFAlWAgVEC\n"
operator|+
literal|"GwMFCQABUYAGCwkIBwMCBhUIAgkKCwQWAgMBAh4BAheAAAoJEPoJoMQwpaBTjhoD\n"
operator|+
literal|"/0MRCX1zBjEKIfzFYeSEg/OcSLbAkUD7un5YTfpgds3oUNIKlIgovWO24TQxrCCu\n"
operator|+
literal|"5pSzN/WfRSzPFhj9HahY/5yh+EGd6HmIU2v/k5I3LwTPEOcZUi1SzOScSv6JOO9Q\n"
operator|+
literal|"3srVilCu3h6TNW1UGBNjfOr1NdmkWfsUZcjsEc/XrfBGuI0EVYCBUQEEAL0UP9jJ\n"
operator|+
literal|"eLj3klCCa2tmwdgyFiSf9T+Yoed4I3v3ag2F0/CWrCJr3e1ogSs4Bdts0WptI+Nu\n"
operator|+
literal|"QIq40AYszewq55dTcB4lbNAYE4svVYQ5AGz78iKzljaBFhyT6ePdZ5wfb+8Jqu1l\n"
operator|+
literal|"7wRwzRI5Jn3OXCmdGm/dmoUNG136EA9A4ZLLABEBAAGIpQQYAQIADwUCVYCBUQIb\n"
operator|+
literal|"DAUJAAFRgAAKCRD6CaDEMKWgU5JTA/9XjwPFZ5NseNROMhYZMmje1/ixISb2jaVc\n"
operator|+
literal|"9m9RLCl8Y3RCY9NNdU5FinTIX9LsRTrJlW6FSG5sin8mwx9jq0eGE1TBEKND5klT\n"
operator|+
literal|"TmsG0jx1dZG9kWDy6lPnIWw2/4W+N0fK/Cw6WEL1Xg7RLi4NQ9Bi2WoxJii9bWMv\n"
operator|+
literal|"yy35U6UfPQ==\n"
operator|+
literal|"=0GL9\n"
operator|+
literal|"-----END PGP PUBLIC KEY BLOCK-----\n"
decl_stmt|;
DECL|field|key
specifier|private
name|PGPPublicKey
name|key
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|ArmoredInputStream
name|in
init|=
operator|new
name|ArmoredInputStream
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|Constants
operator|.
name|encode
argument_list|(
name|PUBKEY
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|PGPPublicKeyRing
name|keyRing
init|=
operator|new
name|PGPPublicKeyRing
argument_list|(
name|in
argument_list|,
operator|new
name|BcKeyFingerprintCalculator
argument_list|()
argument_list|)
decl_stmt|;
name|key
operator|=
name|keyRing
operator|.
name|getPublicKey
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testKeyIdToString ()
specifier|public
name|void
name|testKeyIdToString
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|keyIdToString
argument_list|(
name|key
operator|.
name|getKeyID
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"30A5A053"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testKeyToString ()
specifier|public
name|void
name|testKeyToString
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThat
argument_list|(
name|SignedPushPreReceiveHook
operator|.
name|toString
argument_list|(
name|key
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"30A5A053 A U. Thor<a_u_thor@example.com>"
operator|+
literal|" (96D6 DE78 E6D8 DA49 9387  1F31 FA09 A0C4 30A5 A053)"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testKeyObjectId ()
specifier|public
name|void
name|testKeyObjectId
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|objId
init|=
name|SignedPushPreReceiveHook
operator|.
name|keyObjectId
argument_list|(
name|key
operator|.
name|getKeyID
argument_list|()
argument_list|)
operator|.
name|name
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|objId
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"fa09a0c430a5a053000000000000000000000000"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|objId
operator|.
name|substring
argument_list|(
literal|8
argument_list|,
literal|16
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|keyIdToString
argument_list|(
name|key
operator|.
name|getKeyID
argument_list|()
argument_list|)
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

