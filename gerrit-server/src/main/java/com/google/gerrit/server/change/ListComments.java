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
name|account
operator|.
name|AccountInfo
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

begin_class
DECL|class|ListComments
class|class
name|ListComments
extends|extends
name|ListDrafts
block|{
annotation|@
name|Inject
DECL|method|ListComments (Provider<ReviewDb> db, AccountInfo.Loader.Factory alf)
name|ListComments
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|AccountInfo
operator|.
name|Loader
operator|.
name|Factory
name|alf
parameter_list|)
block|{
name|super
argument_list|(
name|db
argument_list|,
name|alf
argument_list|)
expr_stmt|;
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
DECL|method|listComments (RevisionResource rsrc)
specifier|protected
name|Iterable
argument_list|<
name|PatchLineComment
argument_list|>
name|listComments
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|db
operator|.
name|get
argument_list|()
operator|.
name|patchComments
argument_list|()
operator|.
name|publishedByPatchSet
argument_list|(
name|rsrc
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

