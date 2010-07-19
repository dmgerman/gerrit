begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.events
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|events
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|PatchSetAttribute
specifier|public
class|class
name|PatchSetAttribute
block|{
DECL|field|number
specifier|public
name|String
name|number
decl_stmt|;
DECL|field|revision
specifier|public
name|String
name|revision
decl_stmt|;
DECL|field|ref
specifier|public
name|String
name|ref
decl_stmt|;
DECL|field|uploader
specifier|public
name|AccountAttribute
name|uploader
decl_stmt|;
DECL|field|approvals
specifier|public
name|List
argument_list|<
name|ApprovalAttribute
argument_list|>
name|approvals
decl_stmt|;
block|}
end_class

end_unit

