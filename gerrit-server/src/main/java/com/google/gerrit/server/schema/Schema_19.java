begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|CurrentSchemaVersion
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
name|reviewdb
operator|.
name|ReviewDb
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|ProvisionException
import|;
end_import

begin_class
DECL|class|Schema_19
class|class
name|Schema_19
extends|extends
name|SchemaVersion
block|{
annotation|@
name|Inject
DECL|method|Schema_19 ()
name|Schema_19
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|Provider
argument_list|<
name|SchemaVersion
argument_list|>
argument_list|()
block|{
specifier|public
name|SchemaVersion
name|get
parameter_list|()
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"Cannot upgrade from 18"
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|upgradeFrom (UpdateUI ui, CurrentSchemaVersion curr, ReviewDb db)
specifier|protected
name|void
name|upgradeFrom
parameter_list|(
name|UpdateUI
name|ui
parameter_list|,
name|CurrentSchemaVersion
name|curr
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
literal|"Cannot upgrade from "
operator|+
name|curr
operator|.
name|versionNbr
operator|+
literal|"; manually run scripts from"
operator|+
literal|" http://gerrit.googlecode.com/files/schema-upgrades003_019.zip"
operator|+
literal|" and restart."
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

