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
DECL|package|com.google.gerrit.pgm.http.jetty
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|http
operator|.
name|jetty
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
name|lifecycle
operator|.
name|LifecycleModule
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
name|AbstractModule
import|;
end_import

begin_class
DECL|class|JettyModule
specifier|public
class|class
name|JettyModule
extends|extends
name|AbstractModule
block|{
DECL|field|env
specifier|private
specifier|final
name|JettyEnv
name|env
decl_stmt|;
DECL|method|JettyModule (final JettyEnv env)
specifier|public
name|JettyModule
parameter_list|(
specifier|final
name|JettyEnv
name|env
parameter_list|)
block|{
name|this
operator|.
name|env
operator|=
name|env
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|JettyEnv
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|JettyServer
operator|.
name|class
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|LifecycleModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|JettyServer
operator|.
name|Lifecycle
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

