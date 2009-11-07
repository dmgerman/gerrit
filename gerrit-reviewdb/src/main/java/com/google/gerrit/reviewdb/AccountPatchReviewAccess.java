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
DECL|package|com.google.gerrit.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Access
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
name|client
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
name|gwtorm
operator|.
name|client
operator|.
name|PrimaryKey
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
name|client
operator|.
name|Query
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
name|client
operator|.
name|ResultSet
import|;
end_import

begin_interface
DECL|interface|AccountPatchReviewAccess
specifier|public
interface|interface
name|AccountPatchReviewAccess
extends|extends
name|Access
argument_list|<
name|AccountPatchReview
argument_list|,
name|AccountPatchReview
operator|.
name|Key
argument_list|>
block|{
annotation|@
name|PrimaryKey
argument_list|(
literal|"key"
argument_list|)
DECL|method|get (AccountPatchReview.Key id)
name|AccountPatchReview
name|get
parameter_list|(
name|AccountPatchReview
operator|.
name|Key
name|id
parameter_list|)
throws|throws
name|OrmException
function_decl|;
annotation|@
name|Query
argument_list|(
literal|"WHERE key.accountId = ? AND key.patchKey.patchSetId = ?"
argument_list|)
DECL|method|byReviewer (Account.Id who, PatchSet.Id ps)
name|ResultSet
argument_list|<
name|AccountPatchReview
argument_list|>
name|byReviewer
parameter_list|(
name|Account
operator|.
name|Id
name|who
parameter_list|,
name|PatchSet
operator|.
name|Id
name|ps
parameter_list|)
throws|throws
name|OrmException
function_decl|;
block|}
end_interface

end_unit

