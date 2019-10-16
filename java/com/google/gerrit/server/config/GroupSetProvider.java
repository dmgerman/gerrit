begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
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
name|collect
operator|.
name|ImmutableSet
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
name|flogger
operator|.
name|FluentLogger
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
name|common
operator|.
name|data
operator|.
name|GroupReference
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
name|AccountGroup
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
name|account
operator|.
name|GroupBackend
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
name|account
operator|.
name|GroupBackends
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
name|util
operator|.
name|RequestContext
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
name|util
operator|.
name|ServerRequestContext
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
name|util
operator|.
name|ThreadLocalRequestContext
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/** Parses groups referenced in the {@code gerrit.config} file. */
end_comment

begin_class
DECL|class|GroupSetProvider
specifier|public
specifier|abstract
class|class
name|GroupSetProvider
implements|implements
name|Provider
argument_list|<
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|>
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|field|groupIds
specifier|protected
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groupIds
decl_stmt|;
DECL|method|GroupSetProvider ( GroupBackend groupBackend, ThreadLocalRequestContext threadContext, ServerRequestContext serverCtx, List<String> groupNames)
specifier|protected
name|GroupSetProvider
parameter_list|(
name|GroupBackend
name|groupBackend
parameter_list|,
name|ThreadLocalRequestContext
name|threadContext
parameter_list|,
name|ServerRequestContext
name|serverCtx
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|groupNames
parameter_list|)
block|{
name|RequestContext
name|ctx
init|=
name|threadContext
operator|.
name|setContext
argument_list|(
name|serverCtx
argument_list|)
decl_stmt|;
try|try
block|{
name|ImmutableSet
operator|.
name|Builder
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|builder
init|=
name|ImmutableSet
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|n
range|:
name|groupNames
control|)
block|{
name|GroupReference
name|g
init|=
name|GroupBackends
operator|.
name|findBestSuggestion
argument_list|(
name|groupBackend
argument_list|,
name|n
argument_list|)
decl_stmt|;
if|if
condition|(
name|g
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|add
argument_list|(
name|g
operator|.
name|getUUID
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|log
argument_list|(
literal|"Group \"%s\" not available, skipping."
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
block|}
name|groupIds
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|threadContext
operator|.
name|setContext
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|get
parameter_list|()
block|{
return|return
name|groupIds
return|;
block|}
block|}
end_class

end_unit

