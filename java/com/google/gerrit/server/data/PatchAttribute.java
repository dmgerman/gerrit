begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|data
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
name|Patch
operator|.
name|ChangeType
import|;
end_import

begin_class
DECL|class|PatchAttribute
specifier|public
class|class
name|PatchAttribute
block|{
DECL|field|file
specifier|public
name|String
name|file
decl_stmt|;
DECL|field|fileOld
specifier|public
name|String
name|fileOld
decl_stmt|;
DECL|field|type
specifier|public
name|ChangeType
name|type
decl_stmt|;
DECL|field|insertions
specifier|public
name|int
name|insertions
decl_stmt|;
DECL|field|deletions
specifier|public
name|int
name|deletions
decl_stmt|;
block|}
end_class

end_unit

