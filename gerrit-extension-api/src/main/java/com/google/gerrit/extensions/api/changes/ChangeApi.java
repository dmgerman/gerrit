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
DECL|package|com.google.gerrit.extensions.api.changes
package|package
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
name|changes
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
name|RestApiException
import|;
end_import

begin_interface
DECL|interface|ChangeApi
specifier|public
interface|interface
name|ChangeApi
block|{
DECL|method|id ()
name|String
name|id
parameter_list|()
function_decl|;
DECL|method|current ()
name|RevisionApi
name|current
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|revision (int id)
name|RevisionApi
name|revision
parameter_list|(
name|int
name|id
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|revision (String id)
name|RevisionApi
name|revision
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|abandon ()
name|void
name|abandon
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|abandon (AbandonInput in)
name|void
name|abandon
parameter_list|(
name|AbandonInput
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|restore ()
name|void
name|restore
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|restore (RestoreInput in)
name|void
name|restore
parameter_list|(
name|RestoreInput
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|revert ()
name|ChangeApi
name|revert
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|revert (RevertInput in)
name|ChangeApi
name|revert
parameter_list|(
name|RevertInput
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|addReviewer (AddReviewerInput in)
name|void
name|addReviewer
parameter_list|(
name|AddReviewerInput
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
block|}
end_interface

end_unit

