begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.pgm
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
package|;
end_package

begin_comment
comment|/** Policy for auto upgrading schema on server startup */
end_comment

begin_enum
DECL|enum|SchemaUpgradePolicy
specifier|public
enum|enum
name|SchemaUpgradePolicy
block|{
comment|/** Perform schema migration if necessary and prune unused objects */
DECL|enumConstant|AUTO
name|AUTO
block|,
comment|/** Like AUTO but don't prune unused objects */
DECL|enumConstant|AUTO_NO_PRUNE
name|AUTO_NO_PRUNE
block|,
comment|/** No automatic schema upgrade */
DECL|enumConstant|OFF
name|OFF
block|}
end_enum

end_unit

