begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
package|;
end_package

begin_import
import|import
name|com
operator|.
name|dyuproject
operator|.
name|openid
operator|.
name|OpenIdUser
import|;
end_import

begin_import
import|import
name|com
operator|.
name|dyuproject
operator|.
name|openid
operator|.
name|RelyingParty
import|;
end_import

begin_import
import|import
name|com
operator|.
name|dyuproject
operator|.
name|openid
operator|.
name|manager
operator|.
name|CookieBasedUserManager
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_class
DECL|class|GerritOpenIdUserManager
specifier|public
class|class
name|GerritOpenIdUserManager
extends|extends
name|CookieBasedUserManager
block|{
DECL|method|GerritOpenIdUserManager ()
specifier|public
name|GerritOpenIdUserManager
parameter_list|()
block|{
specifier|final
name|int
name|age
init|=
literal|2
operator|*
literal|60
comment|/* seconds */
decl_stmt|;
name|setCookieName
argument_list|(
literal|"gerrit_openid"
argument_list|)
expr_stmt|;
name|setSecretKey
argument_list|(
literal|"gerrit_openid"
argument_list|)
expr_stmt|;
name|setMaxAge
argument_list|(
name|age
argument_list|)
expr_stmt|;
name|setLoginTimeout
argument_list|(
name|age
argument_list|)
expr_stmt|;
name|setEncrypted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init (Properties properties)
specifier|public
name|void
name|init
parameter_list|(
name|Properties
name|properties
parameter_list|)
block|{   }
annotation|@
name|Override
DECL|method|getUser (final HttpServletRequest request)
specifier|public
name|OpenIdUser
name|getUser
parameter_list|(
specifier|final
name|HttpServletRequest
name|request
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|request
operator|.
name|getParameter
argument_list|(
name|RelyingParty
operator|.
name|DEFAULT_IDENTIFIER_PARAMETER
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|super
operator|.
name|getUser
argument_list|(
name|request
argument_list|)
return|;
block|}
block|}
end_class

end_unit

