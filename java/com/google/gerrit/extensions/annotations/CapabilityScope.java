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
DECL|package|com.google.gerrit.extensions.annotations
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|annotations
package|;
end_package

begin_comment
comment|/** Declared scope of a capability named by {@link RequiresCapability}. */
end_comment

begin_enum
DECL|enum|CapabilityScope
specifier|public
enum|enum
name|CapabilityScope
block|{
comment|/**    * Scope is assumed based on the context.    *    *<p>When {@code @RequiresCapability} is used within a plugin the scope of the capability is    * assumed to be that plugin.    *    *<p>If {@code @RequiresCapability} is used within the core Gerrit Code Review server (and thus    * is outside of a plugin) the scope is the core server and will use {@code    * com.google.gerrit.common.data.GlobalCapability}.    */
DECL|enumConstant|CONTEXT
name|CONTEXT
block|,
comment|/** Scope is only the plugin. */
DECL|enumConstant|PLUGIN
name|PLUGIN
block|,
comment|/** Scope is the core server. */
DECL|enumConstant|CORE
name|CORE
block|}
end_enum

end_unit

