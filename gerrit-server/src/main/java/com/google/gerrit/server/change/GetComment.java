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
name|restapi
operator|.
name|RestReadView
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

begin_class
DECL|class|GetComment
class|class
name|GetComment
implements|implements
name|RestReadView
argument_list|<
name|CommentResource
argument_list|>
block|{
DECL|field|accountLoaderFactory
specifier|private
specifier|final
name|AccountInfo
operator|.
name|Loader
operator|.
name|Factory
name|accountLoaderFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetComment (AccountInfo.Loader.Factory accountLoaderFactory)
name|GetComment
parameter_list|(
name|AccountInfo
operator|.
name|Loader
operator|.
name|Factory
name|accountLoaderFactory
parameter_list|)
block|{
name|this
operator|.
name|accountLoaderFactory
operator|=
name|accountLoaderFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (CommentResource rsrc)
specifier|public
name|Object
name|apply
parameter_list|(
name|CommentResource
name|rsrc
parameter_list|)
throws|throws
name|OrmException
block|{
name|AccountInfo
operator|.
name|Loader
name|accountLoader
init|=
name|accountLoaderFactory
operator|.
name|create
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|CommentInfo
name|ci
init|=
operator|new
name|CommentInfo
argument_list|(
name|rsrc
operator|.
name|getComment
argument_list|()
argument_list|,
name|accountLoader
argument_list|)
decl_stmt|;
name|accountLoader
operator|.
name|fill
argument_list|()
expr_stmt|;
return|return
name|ci
return|;
block|}
block|}
end_class

end_unit

