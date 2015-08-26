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
DECL|package|com.google.gerrit.gpg
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|gpg
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|gpg
operator|.
name|testutil
operator|.
name|TestKey
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
name|util
operator|.
name|Arrays
import|;
end_import

begin_class
DECL|class|PublicKeyCheckerTest
specifier|public
class|class
name|PublicKeyCheckerTest
block|{
DECL|field|checker
specifier|private
name|PublicKeyChecker
name|checker
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|checker
operator|=
operator|new
name|PublicKeyChecker
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|validKey ()
specifier|public
name|void
name|validKey
parameter_list|()
throws|throws
name|Exception
block|{
name|assertProblems
argument_list|(
name|TestKey
operator|.
name|key1
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|wrongKeyId ()
specifier|public
name|void
name|wrongKeyId
parameter_list|()
throws|throws
name|Exception
block|{
name|TestKey
name|k
init|=
name|TestKey
operator|.
name|key1
argument_list|()
decl_stmt|;
name|long
name|badId
init|=
name|k
operator|.
name|getKeyId
argument_list|()
operator|+
literal|1
decl_stmt|;
name|CheckResult
name|result
init|=
name|checker
operator|.
name|check
argument_list|(
name|k
operator|.
name|getPublicKey
argument_list|()
argument_list|,
name|badId
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"Public key does not match ID 46328A8D"
argument_list|)
argument_list|,
name|result
operator|.
name|getProblems
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|keyExpiringInFuture ()
specifier|public
name|void
name|keyExpiringInFuture
parameter_list|()
throws|throws
name|Exception
block|{
name|assertProblems
argument_list|(
name|TestKey
operator|.
name|key2
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|expiredKey ()
specifier|public
name|void
name|expiredKey
parameter_list|()
throws|throws
name|Exception
block|{
name|assertProblems
argument_list|(
name|TestKey
operator|.
name|key3
argument_list|()
argument_list|,
literal|"Key is expired"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|selfRevokedKey ()
specifier|public
name|void
name|selfRevokedKey
parameter_list|()
throws|throws
name|Exception
block|{
name|assertProblems
argument_list|(
name|TestKey
operator|.
name|key4
argument_list|()
argument_list|,
literal|"Key is revoked"
argument_list|)
expr_stmt|;
block|}
DECL|method|assertProblems (TestKey tk, String... expected)
specifier|private
name|void
name|assertProblems
parameter_list|(
name|TestKey
name|tk
parameter_list|,
name|String
modifier|...
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|CheckResult
name|result
init|=
name|checker
operator|.
name|check
argument_list|(
name|tk
operator|.
name|getPublicKey
argument_list|()
argument_list|,
name|tk
operator|.
name|getKeyId
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|expected
argument_list|)
argument_list|,
name|result
operator|.
name|getProblems
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

