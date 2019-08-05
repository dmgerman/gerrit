begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
name|reviewdb
operator|.
name|client
operator|.
name|Comment
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
name|CommentsUtil
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
name|query
operator|.
name|change
operator|.
name|ChangeData
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
DECL|class|ListChangeComments
specifier|public
class|class
name|ListChangeComments
extends|extends
name|ListChangeDrafts
block|{
annotation|@
name|Inject
DECL|method|ListChangeComments ( Provider<ReviewDb> db, ChangeData.Factory changeDataFactory, Provider<CommentJson> commentJson, CommentsUtil commentsUtil)
name|ListChangeComments
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
parameter_list|,
name|Provider
argument_list|<
name|CommentJson
argument_list|>
name|commentJson
parameter_list|,
name|CommentsUtil
name|commentsUtil
parameter_list|)
block|{
name|super
argument_list|(
name|db
argument_list|,
name|changeDataFactory
argument_list|,
name|commentJson
argument_list|,
name|commentsUtil
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|listComments (ChangeResource rsrc)
specifier|protected
name|Iterable
argument_list|<
name|Comment
argument_list|>
name|listComments
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|)
throws|throws
name|OrmException
block|{
name|ChangeData
name|cd
init|=
name|changeDataFactory
operator|.
name|create
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getNotes
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|commentsUtil
operator|.
name|publishedByChange
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|cd
operator|.
name|notes
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|includeAuthorInfo ()
specifier|protected
name|boolean
name|includeAuthorInfo
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|requireAuthentication ()
specifier|public
name|boolean
name|requireAuthentication
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

