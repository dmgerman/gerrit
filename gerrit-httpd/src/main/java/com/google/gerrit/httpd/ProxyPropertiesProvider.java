begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
package|;
end_package

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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|GerritServerConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|Config
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|ProxyPropertiesProvider
class|class
name|ProxyPropertiesProvider
implements|implements
name|Provider
argument_list|<
name|ProxyProperties
argument_list|>
block|{
DECL|field|proxyUrl
specifier|private
name|URL
name|proxyUrl
decl_stmt|;
DECL|field|proxyUser
specifier|private
name|String
name|proxyUser
decl_stmt|;
DECL|field|proxyPassword
specifier|private
name|String
name|proxyPassword
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProxyPropertiesProvider (@erritServerConfig Config config)
name|ProxyPropertiesProvider
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|)
throws|throws
name|MalformedURLException
block|{
name|String
name|proxyUrlStr
init|=
name|config
operator|.
name|getString
argument_list|(
literal|"http"
argument_list|,
literal|null
argument_list|,
literal|"proxy"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|proxyUrlStr
argument_list|)
condition|)
block|{
name|proxyUrl
operator|=
operator|new
name|URL
argument_list|(
name|proxyUrlStr
argument_list|)
expr_stmt|;
name|proxyUser
operator|=
name|config
operator|.
name|getString
argument_list|(
literal|"http"
argument_list|,
literal|null
argument_list|,
literal|"proxyUsername"
argument_list|)
expr_stmt|;
name|proxyPassword
operator|=
name|config
operator|.
name|getString
argument_list|(
literal|"http"
argument_list|,
literal|null
argument_list|,
literal|"proxyPassword"
argument_list|)
expr_stmt|;
name|String
name|userInfo
init|=
name|proxyUrl
operator|.
name|getUserInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|userInfo
operator|!=
literal|null
condition|)
block|{
name|int
name|c
init|=
name|userInfo
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
literal|0
operator|<
name|c
condition|)
block|{
name|proxyUser
operator|=
name|userInfo
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|c
argument_list|)
expr_stmt|;
name|proxyPassword
operator|=
name|userInfo
operator|.
name|substring
argument_list|(
name|c
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|proxyUser
operator|=
name|userInfo
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|ProxyProperties
name|get
parameter_list|()
block|{
return|return
operator|new
name|ProxyProperties
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|URL
name|getProxyUrl
parameter_list|()
block|{
return|return
name|proxyUrl
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|proxyUser
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|proxyPassword
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

