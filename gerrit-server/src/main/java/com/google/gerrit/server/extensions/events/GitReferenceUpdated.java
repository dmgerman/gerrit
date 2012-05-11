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
DECL|package|com.google.gerrit.server.extensions.events
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|extensions
operator|.
name|events
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
name|gerrit
operator|.
name|extensions
operator|.
name|events
operator|.
name|GitReferenceUpdatedListener
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
name|registration
operator|.
name|DynamicSet
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|java
operator|.
name|util
operator|.
name|Collections
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

begin_class
DECL|class|GitReferenceUpdated
specifier|public
class|class
name|GitReferenceUpdated
block|{
DECL|field|DISABLED
specifier|public
specifier|static
specifier|final
name|GitReferenceUpdated
name|DISABLED
init|=
operator|new
name|GitReferenceUpdated
argument_list|(
name|Collections
operator|.
expr|<
name|GitReferenceUpdatedListener
operator|>
name|emptyList
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|listeners
specifier|private
specifier|final
name|Iterable
argument_list|<
name|GitReferenceUpdatedListener
argument_list|>
name|listeners
decl_stmt|;
annotation|@
name|Inject
DECL|method|GitReferenceUpdated (DynamicSet<GitReferenceUpdatedListener> listeners)
name|GitReferenceUpdated
parameter_list|(
name|DynamicSet
argument_list|<
name|GitReferenceUpdatedListener
argument_list|>
name|listeners
parameter_list|)
block|{
name|this
operator|.
name|listeners
operator|=
name|listeners
expr_stmt|;
block|}
DECL|method|GitReferenceUpdated (Iterable<GitReferenceUpdatedListener> listeners)
name|GitReferenceUpdated
parameter_list|(
name|Iterable
argument_list|<
name|GitReferenceUpdatedListener
argument_list|>
name|listeners
parameter_list|)
block|{
name|this
operator|.
name|listeners
operator|=
name|listeners
expr_stmt|;
block|}
DECL|method|fire (Project.NameKey project, String ref)
specifier|public
name|void
name|fire
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|String
name|ref
parameter_list|)
block|{
name|Event
name|event
init|=
operator|new
name|Event
argument_list|(
name|project
argument_list|,
name|ref
argument_list|)
decl_stmt|;
for|for
control|(
name|GitReferenceUpdatedListener
name|l
range|:
name|listeners
control|)
block|{
name|l
operator|.
name|onGitReferenceUpdated
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|Event
specifier|private
specifier|static
class|class
name|Event
implements|implements
name|GitReferenceUpdatedListener
operator|.
name|Event
block|{
DECL|field|projectName
specifier|private
specifier|final
name|String
name|projectName
decl_stmt|;
DECL|field|ref
specifier|private
specifier|final
name|String
name|ref
decl_stmt|;
DECL|method|Event (Project.NameKey project, String ref)
name|Event
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|projectName
operator|=
name|project
operator|.
name|get
argument_list|()
expr_stmt|;
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getProjectName ()
specifier|public
name|String
name|getProjectName
parameter_list|()
block|{
return|return
name|projectName
return|;
block|}
annotation|@
name|Override
DECL|method|getUpdates ()
specifier|public
name|List
argument_list|<
name|GitReferenceUpdatedListener
operator|.
name|Update
argument_list|>
name|getUpdates
parameter_list|()
block|{
name|GitReferenceUpdatedListener
operator|.
name|Update
name|update
init|=
operator|new
name|GitReferenceUpdatedListener
operator|.
name|Update
argument_list|()
block|{
specifier|public
name|String
name|getRefName
parameter_list|()
block|{
return|return
name|ref
return|;
block|}
block|}
decl_stmt|;
return|return
name|ImmutableList
operator|.
name|of
argument_list|(
name|update
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

