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
DECL|package|com.google.gerrit.httpd.rpc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|rpc
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|RemoteJsonService
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
name|Key
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
name|Scopes
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
name|internal
operator|.
name|UniqueAnnotations
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
comment|/** Binds {@link RemoteJsonService} implementations to a JSON servlet. */
end_comment

begin_class
DECL|class|RpcServletModule
specifier|public
specifier|abstract
class|class
name|RpcServletModule
extends|extends
name|ServletModule
block|{
DECL|field|PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|PREFIX
init|=
literal|"/gerrit/rpc/"
decl_stmt|;
DECL|field|prefix
specifier|private
specifier|final
name|String
name|prefix
decl_stmt|;
DECL|method|RpcServletModule (final String pathPrefix)
specifier|protected
name|RpcServletModule
parameter_list|(
specifier|final
name|String
name|pathPrefix
parameter_list|)
block|{
name|prefix
operator|=
name|pathPrefix
expr_stmt|;
block|}
DECL|method|rpc (Class<? extends RemoteJsonService> clazz)
specifier|protected
name|void
name|rpc
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|RemoteJsonService
argument_list|>
name|clazz
parameter_list|)
block|{
name|String
name|name
init|=
name|clazz
operator|.
name|getSimpleName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|"Impl"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
block|}
name|rpc
argument_list|(
name|name
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
DECL|method|rpc (final String name, Class<? extends RemoteJsonService> clazz)
specifier|protected
name|void
name|rpc
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|RemoteJsonService
argument_list|>
name|clazz
parameter_list|)
block|{
specifier|final
name|Key
argument_list|<
name|GerritJsonServlet
argument_list|>
name|srv
init|=
name|Key
operator|.
name|get
argument_list|(
name|GerritJsonServlet
operator|.
name|class
argument_list|,
name|UniqueAnnotations
operator|.
name|create
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|GerritJsonServletProvider
name|provider
init|=
operator|new
name|GerritJsonServletProvider
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|bind
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
name|serve
argument_list|(
name|prefix
operator|+
name|name
argument_list|)
operator|.
name|with
argument_list|(
name|srv
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|srv
argument_list|)
operator|.
name|toProvider
argument_list|(
name|provider
argument_list|)
operator|.
name|in
argument_list|(
name|Scopes
operator|.
name|SINGLETON
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

