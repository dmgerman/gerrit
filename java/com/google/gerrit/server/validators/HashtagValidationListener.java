begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.validators
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|validators
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
name|entities
operator|.
name|Change
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
name|annotations
operator|.
name|ExtensionPoint
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
comment|/** Listener to provide validation of hashtag changes. */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|HashtagValidationListener
specifier|public
interface|interface
name|HashtagValidationListener
block|{
comment|/**    * Invoked by Gerrit before hashtags are changed.    *    * @param change the change on which the hashtags are changed    * @param toAdd the hashtags to be added    * @param toRemove the hashtags to be removed    * @throws ValidationException if validation fails    */
DECL|method|validateHashtags (Change change, Set<String> toAdd, Set<String> toRemove)
name|void
name|validateHashtags
parameter_list|(
name|Change
name|change
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|toAdd
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|toRemove
parameter_list|)
throws|throws
name|ValidationException
function_decl|;
block|}
end_interface

end_unit

