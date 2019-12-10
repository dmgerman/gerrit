begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.diff
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|diff
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
name|common
operator|.
name|DiffWebLinkInfo
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
name|common
operator|.
name|WebLinkInfo
import|;
end_import

begin_comment
comment|/** Provider for different types of links which can be displayed in a diff view. */
end_comment

begin_interface
DECL|interface|DiffWebLinksProvider
specifier|public
interface|interface
name|DiffWebLinksProvider
block|{
comment|/** Returns links associated with the diff view */
DECL|method|getDiffLinks ()
name|ImmutableList
argument_list|<
name|DiffWebLinkInfo
argument_list|>
name|getDiffLinks
parameter_list|()
function_decl|;
comment|/** Returns links associated with the diff side */
DECL|method|getFileWebLinks (DiffSide.Type fileInfoType)
name|ImmutableList
argument_list|<
name|WebLinkInfo
argument_list|>
name|getFileWebLinks
parameter_list|(
name|DiffSide
operator|.
name|Type
name|fileInfoType
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

