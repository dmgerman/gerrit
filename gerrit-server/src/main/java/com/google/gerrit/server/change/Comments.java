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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicMap
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
name|restapi
operator|.
name|ChildCollection
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
name|restapi
operator|.
name|IdString
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
name|restapi
operator|.
name|ResourceNotFoundException
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
name|restapi
operator|.
name|RestView
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
name|PatchLineComment
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
name|server
operator|.
name|ReviewDb
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
name|PatchLineCommentsUtil
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
name|notedb
operator|.
name|ChangeNotes
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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

begin_class
annotation|@
name|Singleton
DECL|class|Comments
specifier|public
class|class
name|Comments
implements|implements
name|ChildCollection
argument_list|<
name|RevisionResource
argument_list|,
name|CommentResource
argument_list|>
block|{
DECL|field|views
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|CommentResource
argument_list|>
argument_list|>
name|views
decl_stmt|;
DECL|field|list
specifier|private
specifier|final
name|ListComments
name|list
decl_stmt|;
DECL|field|dbProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|plcUtil
specifier|private
specifier|final
name|PatchLineCommentsUtil
name|plcUtil
decl_stmt|;
annotation|@
name|Inject
DECL|method|Comments (DynamicMap<RestView<CommentResource>> views, ListComments list, Provider<ReviewDb> dbProvider, PatchLineCommentsUtil plcUtil)
name|Comments
parameter_list|(
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|CommentResource
argument_list|>
argument_list|>
name|views
parameter_list|,
name|ListComments
name|list
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|PatchLineCommentsUtil
name|plcUtil
parameter_list|)
block|{
name|this
operator|.
name|views
operator|=
name|views
expr_stmt|;
name|this
operator|.
name|list
operator|=
name|list
expr_stmt|;
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
name|this
operator|.
name|plcUtil
operator|=
name|plcUtil
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|views ()
specifier|public
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|CommentResource
argument_list|>
argument_list|>
name|views
parameter_list|()
block|{
return|return
name|views
return|;
block|}
annotation|@
name|Override
DECL|method|list ()
specifier|public
name|ListComments
name|list
parameter_list|()
block|{
return|return
name|list
return|;
block|}
annotation|@
name|Override
DECL|method|parse (RevisionResource rev, IdString id)
specifier|public
name|CommentResource
name|parse
parameter_list|(
name|RevisionResource
name|rev
parameter_list|,
name|IdString
name|id
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|OrmException
block|{
name|String
name|uuid
init|=
name|id
operator|.
name|get
argument_list|()
decl_stmt|;
name|ChangeNotes
name|notes
init|=
name|rev
operator|.
name|getNotes
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchLineComment
name|c
range|:
name|plcUtil
operator|.
name|publishedByPatchSet
argument_list|(
name|dbProvider
operator|.
name|get
argument_list|()
argument_list|,
name|notes
argument_list|,
name|rev
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
name|uuid
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|CommentResource
argument_list|(
name|rev
argument_list|,
name|c
argument_list|)
return|;
block|}
block|}
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

