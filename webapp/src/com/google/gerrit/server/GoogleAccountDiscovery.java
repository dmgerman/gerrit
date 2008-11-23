begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
name|Discovery
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
name|OpenIdContext
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
name|OpenIdUser
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/** Discovery support for Google Accounts and other standard OpenID providers */
end_comment

begin_class
DECL|class|GoogleAccountDiscovery
specifier|public
class|class
name|GoogleAccountDiscovery
implements|implements
name|Discovery
block|{
comment|/** OpenID discovery end-point for Google Accounts */
DECL|field|GOOGLE_ACCOUNT
specifier|public
specifier|static
specifier|final
name|String
name|GOOGLE_ACCOUNT
init|=
literal|"https://www.google.com/accounts/o8/id"
decl_stmt|;
DECL|field|base
specifier|private
specifier|final
name|Discovery
name|base
decl_stmt|;
DECL|method|GoogleAccountDiscovery (final Discovery base)
specifier|public
name|GoogleAccountDiscovery
parameter_list|(
specifier|final
name|Discovery
name|base
parameter_list|)
block|{
name|this
operator|.
name|base
operator|=
name|base
expr_stmt|;
block|}
DECL|method|discover (final String claimedId, final OpenIdContext context)
specifier|public
name|OpenIdUser
name|discover
parameter_list|(
specifier|final
name|String
name|claimedId
parameter_list|,
specifier|final
name|OpenIdContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|GOOGLE_ACCOUNT
operator|.
name|equals
argument_list|(
name|claimedId
argument_list|)
condition|)
block|{
comment|// TODO We shouldn't hard-code the XRDS discovery result.
comment|//
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|m
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
literal|"ci"
argument_list|,
name|claimedId
argument_list|)
expr_stmt|;
name|m
operator|.
name|put
argument_list|(
literal|"os"
argument_list|,
literal|"https://www.google.com/accounts/o8/ud"
argument_list|)
expr_stmt|;
specifier|final
name|OpenIdUser
name|u
init|=
operator|new
name|OpenIdUser
argument_list|()
decl_stmt|;
name|u
operator|.
name|fromJSON
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|u
return|;
block|}
return|return
name|base
operator|.
name|discover
argument_list|(
name|claimedId
argument_list|,
name|context
argument_list|)
return|;
block|}
block|}
end_class

end_unit

