begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
package|;
end_package

begin_class
DECL|class|Schema_180
specifier|public
class|class
name|Schema_180
implements|implements
name|NoteDbSchemaVersion
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|method|Schema_180 (Arguments args)
name|Schema_180
parameter_list|(
name|Arguments
name|args
parameter_list|)
block|{
comment|// Do nothing.
block|}
annotation|@
name|Override
DECL|method|upgrade (UpdateUI ui)
specifier|public
name|void
name|upgrade
parameter_list|(
name|UpdateUI
name|ui
parameter_list|)
block|{
comment|// Do nothing; only used to populate the version ref, which is done by the caller.
block|}
block|}
end_class

end_unit

