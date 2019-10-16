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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|GitOverHttpServlet
operator|.
name|URL_REGEX
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
name|entities
operator|.
name|CoreDownloadSchemes
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
name|AuthConfig
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
name|DownloadConfig
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
name|servlet
operator|.
name|ServletModule
import|;
end_import

begin_comment
comment|/** Configures Git access over HTTP with authentication. */
end_comment

begin_class
DECL|class|GitOverHttpModule
specifier|public
class|class
name|GitOverHttpModule
extends|extends
name|ServletModule
block|{
DECL|field|authConfig
specifier|private
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
DECL|field|downloadConfig
specifier|private
specifier|final
name|DownloadConfig
name|downloadConfig
decl_stmt|;
annotation|@
name|Inject
DECL|method|GitOverHttpModule (AuthConfig authConfig, DownloadConfig downloadConfig)
name|GitOverHttpModule
parameter_list|(
name|AuthConfig
name|authConfig
parameter_list|,
name|DownloadConfig
name|downloadConfig
parameter_list|)
block|{
name|this
operator|.
name|authConfig
operator|=
name|authConfig
expr_stmt|;
name|this
operator|.
name|downloadConfig
operator|=
name|downloadConfig
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configureServlets ()
specifier|protected
name|void
name|configureServlets
parameter_list|()
block|{
if|if
condition|(
name|downloadConfig
operator|.
name|getDownloadSchemes
argument_list|()
operator|.
name|contains
argument_list|(
name|CoreDownloadSchemes
operator|.
name|ANON_HTTP
argument_list|)
operator|||
name|downloadConfig
operator|.
name|getDownloadSchemes
argument_list|()
operator|.
name|contains
argument_list|(
name|CoreDownloadSchemes
operator|.
name|HTTP
argument_list|)
condition|)
block|{
name|filterRegex
argument_list|(
name|URL_REGEX
argument_list|)
operator|.
name|through
argument_list|(
name|GerritAuthModule
operator|.
name|retreiveAuthFilterFromConfig
argument_list|(
name|authConfig
argument_list|)
argument_list|)
expr_stmt|;
name|serveRegex
argument_list|(
name|URL_REGEX
argument_list|)
operator|.
name|with
argument_list|(
name|GitOverHttpServlet
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

