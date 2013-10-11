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
DECL|package|com.google.gerrit.client.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|change
package|;
end_package

begin_interface
DECL|interface|Messages
specifier|public
interface|interface
name|Messages
extends|extends
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|i18n
operator|.
name|client
operator|.
name|Messages
block|{
DECL|method|relatedChanges (int count)
name|String
name|relatedChanges
parameter_list|(
name|int
name|count
parameter_list|)
function_decl|;
DECL|method|relatedChanges (String count)
name|String
name|relatedChanges
parameter_list|(
name|String
name|count
parameter_list|)
function_decl|;
DECL|method|conflictingChanges (int count)
name|String
name|conflictingChanges
parameter_list|(
name|int
name|count
parameter_list|)
function_decl|;
DECL|method|conflictingChanges (String count)
name|String
name|conflictingChanges
parameter_list|(
name|String
name|count
parameter_list|)
function_decl|;
DECL|method|cherryPicks (int count)
name|String
name|cherryPicks
parameter_list|(
name|int
name|count
parameter_list|)
function_decl|;
DECL|method|cherryPicks (String count)
name|String
name|cherryPicks
parameter_list|(
name|String
name|count
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

