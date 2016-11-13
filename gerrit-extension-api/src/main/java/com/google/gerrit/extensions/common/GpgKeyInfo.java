begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
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
DECL|class|GpgKeyInfo
specifier|public
class|class
name|GpgKeyInfo
block|{
comment|/**    * Status of checking an object like a key or signature.    *    *<p>Order of values in this enum is significant: OK is "better" than BAD, etc.    */
DECL|enum|Status
specifier|public
enum|enum
name|Status
block|{
comment|/** Something is wrong with this key. */
DECL|enumConstant|BAD
name|BAD
block|,
comment|/**      * Inspecting only this key found no problems, but the system does not fully trust the key's      * origin.      */
DECL|enumConstant|OK
name|OK
block|,
comment|/** This key is valid, and the system knows enough about the key and its origin to trust it. */
DECL|enumConstant|TRUSTED
name|TRUSTED
block|;   }
DECL|field|id
specifier|public
name|String
name|id
decl_stmt|;
DECL|field|fingerprint
specifier|public
name|String
name|fingerprint
decl_stmt|;
DECL|field|userIds
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|userIds
decl_stmt|;
DECL|field|key
specifier|public
name|String
name|key
decl_stmt|;
DECL|field|status
specifier|public
name|Status
name|status
decl_stmt|;
DECL|field|problems
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|problems
decl_stmt|;
block|}
end_class

end_unit

