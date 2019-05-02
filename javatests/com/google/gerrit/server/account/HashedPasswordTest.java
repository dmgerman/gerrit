begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|testing
operator|.
name|GerritJUnit
operator|.
name|assertThrows
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|codec
operator|.
name|DecoderException
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

begin_class
DECL|class|HashedPasswordTest
specifier|public
class|class
name|HashedPasswordTest
block|{
annotation|@
name|Test
DECL|method|encodeOneLine ()
specifier|public
name|void
name|encodeOneLine
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|password
init|=
literal|"secret"
decl_stmt|;
name|HashedPassword
name|hashed
init|=
name|HashedPassword
operator|.
name|fromPassword
argument_list|(
name|password
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|hashed
operator|.
name|encode
argument_list|()
argument_list|)
operator|.
name|doesNotContain
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|hashed
operator|.
name|encode
argument_list|()
argument_list|)
operator|.
name|doesNotContain
argument_list|(
literal|"\r"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|encodeDecode ()
specifier|public
name|void
name|encodeDecode
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|password
init|=
literal|"secret"
decl_stmt|;
name|HashedPassword
name|hashed
init|=
name|HashedPassword
operator|.
name|fromPassword
argument_list|(
name|password
argument_list|)
decl_stmt|;
name|HashedPassword
name|roundtrip
init|=
name|HashedPassword
operator|.
name|decode
argument_list|(
name|hashed
operator|.
name|encode
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|hashed
operator|.
name|encode
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|roundtrip
operator|.
name|encode
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|roundtrip
operator|.
name|checkPassword
argument_list|(
name|password
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|roundtrip
operator|.
name|checkPassword
argument_list|(
literal|"not the password"
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|invalidDecode ()
specifier|public
name|void
name|invalidDecode
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThrows
argument_list|(
name|DecoderException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|HashedPassword
operator|.
name|decode
argument_list|(
literal|"invalid"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|lengthLimit ()
specifier|public
name|void
name|lengthLimit
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|password
init|=
name|Strings
operator|.
name|repeat
argument_list|(
literal|"1"
argument_list|,
literal|72
argument_list|)
decl_stmt|;
comment|// make sure it fits in varchar(255).
name|assertThat
argument_list|(
name|HashedPassword
operator|.
name|fromPassword
argument_list|(
name|password
argument_list|)
operator|.
name|encode
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|isLessThan
argument_list|(
literal|255
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|basicFunctionality ()
specifier|public
name|void
name|basicFunctionality
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|password
init|=
literal|"secret"
decl_stmt|;
name|HashedPassword
name|hashed
init|=
name|HashedPassword
operator|.
name|fromPassword
argument_list|(
name|password
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|hashed
operator|.
name|checkPassword
argument_list|(
literal|"false"
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|hashed
operator|.
name|checkPassword
argument_list|(
name|password
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

