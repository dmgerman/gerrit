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
name|common
operator|.
name|Input
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
name|DynamicItem
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
name|Response
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
name|RestModifyView
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
name|Singleton
import|;
end_import

begin_class
DECL|class|Reviewed
specifier|public
class|class
name|Reviewed
block|{
annotation|@
name|Singleton
DECL|class|PutReviewed
specifier|public
specifier|static
class|class
name|PutReviewed
implements|implements
name|RestModifyView
argument_list|<
name|FileResource
argument_list|,
name|Input
argument_list|>
block|{
DECL|field|accountPatchReviewStore
specifier|private
specifier|final
name|DynamicItem
argument_list|<
name|AccountPatchReviewStore
argument_list|>
name|accountPatchReviewStore
decl_stmt|;
annotation|@
name|Inject
DECL|method|PutReviewed (DynamicItem<AccountPatchReviewStore> accountPatchReviewStore)
name|PutReviewed
parameter_list|(
name|DynamicItem
argument_list|<
name|AccountPatchReviewStore
argument_list|>
name|accountPatchReviewStore
parameter_list|)
block|{
name|this
operator|.
name|accountPatchReviewStore
operator|=
name|accountPatchReviewStore
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (FileResource resource, Input input)
specifier|public
name|Response
argument_list|<
name|String
argument_list|>
name|apply
parameter_list|(
name|FileResource
name|resource
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|accountPatchReviewStore
operator|.
name|get
argument_list|()
operator|.
name|markReviewed
argument_list|(
name|resource
operator|.
name|getPatchKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|resource
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|resource
operator|.
name|getPatchKey
argument_list|()
operator|.
name|getFileName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|Response
operator|.
name|created
argument_list|(
literal|""
argument_list|)
return|;
block|}
return|return
name|Response
operator|.
name|ok
argument_list|(
literal|""
argument_list|)
return|;
block|}
block|}
annotation|@
name|Singleton
DECL|class|DeleteReviewed
specifier|public
specifier|static
class|class
name|DeleteReviewed
implements|implements
name|RestModifyView
argument_list|<
name|FileResource
argument_list|,
name|Input
argument_list|>
block|{
DECL|field|accountPatchReviewStore
specifier|private
specifier|final
name|DynamicItem
argument_list|<
name|AccountPatchReviewStore
argument_list|>
name|accountPatchReviewStore
decl_stmt|;
annotation|@
name|Inject
DECL|method|DeleteReviewed (DynamicItem<AccountPatchReviewStore> accountPatchReviewStore)
name|DeleteReviewed
parameter_list|(
name|DynamicItem
argument_list|<
name|AccountPatchReviewStore
argument_list|>
name|accountPatchReviewStore
parameter_list|)
block|{
name|this
operator|.
name|accountPatchReviewStore
operator|=
name|accountPatchReviewStore
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (FileResource resource, Input input)
specifier|public
name|Response
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|FileResource
name|resource
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|OrmException
block|{
name|accountPatchReviewStore
operator|.
name|get
argument_list|()
operator|.
name|clearReviewed
argument_list|(
name|resource
operator|.
name|getPatchKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|resource
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|resource
operator|.
name|getPatchKey
argument_list|()
operator|.
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|none
argument_list|()
return|;
block|}
block|}
DECL|method|Reviewed ()
specifier|private
name|Reviewed
parameter_list|()
block|{}
block|}
end_class

end_unit
