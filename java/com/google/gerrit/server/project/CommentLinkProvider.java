begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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
name|ImmutableList
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
name|collect
operator|.
name|Lists
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
name|collect
operator|.
name|Multimap
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
name|extensions
operator|.
name|api
operator|.
name|projects
operator|.
name|CommentLinkInfo
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
name|ConfigUpdatedEvent
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
name|ConfigUpdatedEvent
operator|.
name|ConfigUpdateEntry
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
name|ConfigUpdatedEvent
operator|.
name|UpdateResult
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
name|GerritConfigListener
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
DECL|class|CommentLinkProvider
specifier|public
class|class
name|CommentLinkProvider
implements|implements
name|Provider
argument_list|<
name|List
argument_list|<
name|CommentLinkInfo
argument_list|>
argument_list|>
implements|,
name|GerritConfigListener
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
DECL|field|commentLinks
specifier|private
specifier|volatile
name|List
argument_list|<
name|CommentLinkInfo
argument_list|>
name|commentLinks
decl_stmt|;
annotation|@
name|Inject
DECL|method|CommentLinkProvider (@erritServerConfig Config cfg)
name|CommentLinkProvider
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|this
operator|.
name|commentLinks
operator|=
name|parseConfig
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
block|}
DECL|method|parseConfig (Config cfg)
specifier|private
name|List
argument_list|<
name|CommentLinkInfo
argument_list|>
name|parseConfig
parameter_list|(
name|Config
name|cfg
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|subsections
init|=
name|cfg
operator|.
name|getSubsections
argument_list|(
name|ProjectConfig
operator|.
name|COMMENTLINK
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|CommentLinkInfo
argument_list|>
name|cls
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|subsections
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|subsections
control|)
block|{
try|try
block|{
name|CommentLinkInfoImpl
name|cl
init|=
name|ProjectConfig
operator|.
name|buildCommentLink
argument_list|(
name|cfg
argument_list|,
name|name
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|cl
operator|.
name|isOverrideOnly
argument_list|()
condition|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|log
argument_list|(
literal|"commentlink %s empty except for \"enabled\""
argument_list|,
name|name
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|cls
operator|.
name|add
argument_list|(
name|cl
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|log
argument_list|(
literal|"invalid commentlink: %s"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|List
argument_list|<
name|CommentLinkInfo
argument_list|>
name|get
parameter_list|()
block|{
return|return
name|commentLinks
return|;
block|}
annotation|@
name|Override
DECL|method|configUpdated (ConfigUpdatedEvent event)
specifier|public
name|Multimap
argument_list|<
name|UpdateResult
argument_list|,
name|ConfigUpdateEntry
argument_list|>
name|configUpdated
parameter_list|(
name|ConfigUpdatedEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|isSectionUpdated
argument_list|(
name|ProjectConfig
operator|.
name|COMMENTLINK
argument_list|)
condition|)
block|{
name|commentLinks
operator|=
name|parseConfig
argument_list|(
name|event
operator|.
name|getNewConfig
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|event
operator|.
name|accept
argument_list|(
name|ProjectConfig
operator|.
name|COMMENTLINK
argument_list|)
return|;
block|}
return|return
name|ConfigUpdatedEvent
operator|.
name|NO_UPDATES
return|;
block|}
block|}
end_class

end_unit

