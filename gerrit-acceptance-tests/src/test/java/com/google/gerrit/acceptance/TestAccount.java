begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Account
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|KeyPair
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
name|PersonIdent
import|;
end_import

begin_class
DECL|class|TestAccount
specifier|public
class|class
name|TestAccount
block|{
DECL|field|id
specifier|public
specifier|final
name|Account
operator|.
name|Id
name|id
decl_stmt|;
DECL|field|username
specifier|public
specifier|final
name|String
name|username
decl_stmt|;
DECL|field|email
specifier|public
specifier|final
name|String
name|email
decl_stmt|;
DECL|field|fullName
specifier|public
specifier|final
name|String
name|fullName
decl_stmt|;
DECL|field|sshKey
specifier|public
specifier|final
name|KeyPair
name|sshKey
decl_stmt|;
DECL|field|httpPassword
specifier|public
specifier|final
name|String
name|httpPassword
decl_stmt|;
DECL|method|TestAccount (Account.Id id, String username, String email, String fullName, KeyPair sshKey, String httpPassword)
name|TestAccount
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|email
parameter_list|,
name|String
name|fullName
parameter_list|,
name|KeyPair
name|sshKey
parameter_list|,
name|String
name|httpPassword
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
name|this
operator|.
name|email
operator|=
name|email
expr_stmt|;
name|this
operator|.
name|fullName
operator|=
name|fullName
expr_stmt|;
name|this
operator|.
name|sshKey
operator|=
name|sshKey
expr_stmt|;
name|this
operator|.
name|httpPassword
operator|=
name|httpPassword
expr_stmt|;
block|}
DECL|method|privateKey ()
specifier|public
name|byte
index|[]
name|privateKey
parameter_list|()
block|{
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|sshKey
operator|.
name|writePrivateKey
argument_list|(
name|out
argument_list|)
expr_stmt|;
return|return
name|out
operator|.
name|toByteArray
argument_list|()
return|;
block|}
DECL|method|getIdent ()
specifier|public
name|PersonIdent
name|getIdent
parameter_list|()
block|{
return|return
operator|new
name|PersonIdent
argument_list|(
name|username
argument_list|,
name|email
argument_list|)
return|;
block|}
DECL|method|getHttpUrl (GerritServer server)
specifier|public
name|String
name|getHttpUrl
parameter_list|(
name|GerritServer
name|server
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"http://%s:%s@%s:%d"
argument_list|,
name|username
argument_list|,
name|httpPassword
argument_list|,
name|server
operator|.
name|getHttpAddress
argument_list|()
operator|.
name|getAddress
argument_list|()
operator|.
name|getHostAddress
argument_list|()
argument_list|,
name|server
operator|.
name|getHttpAddress
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

